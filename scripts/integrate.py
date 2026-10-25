#!/usr/bin/env python3
"""Integrate this bundle into an existing Android source tree; check by default."""
from pathlib import Path
import argparse
import gzip
import hashlib
import json
import os
import shutil
import subprocess
import sys

BUNDLE = Path(__file__).resolve().parents[1]


def digest(path):
    if path.is_symlink():
        return hashlib.sha256(os.fsencode(os.readlink(path))).hexdigest()
    if not path.exists():
        return None
    h = hashlib.sha256()
    with path.open('rb') as stream:
        for chunk in iter(lambda: stream.read(1024 * 1024), b''):
            h.update(chunk)
    return h.hexdigest()


def child(root, name):
    p = root / name
    if not p.resolve().is_relative_to(root):
        raise ValueError(f'Path escapes Android source root: {name}')
    return p


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('android_root', type=Path)
    parser.add_argument('--apply', action='store_true', help='Apply the fully checked plan')
    args = parser.parse_args()
    root = args.android_root.resolve()
    if root == BUNDLE or BUNDLE.is_relative_to(root):
        raise ValueError('Clone this combined repository outside the Android source tree.')
    if not (root / '.repo').is_dir() or not (root / 'build/envsetup.sh').is_file():
        raise ValueError('Expected an existing Android repo checkout with build/envsetup.sh.')
    inventory = json.loads((BUNDLE / 'integration/install-files.json').read_text())
    locks = json.loads((BUNDLE / 'integration/source-lock.json').read_text())
    errors, copies, assets, patches = [], [], [], []
    for name in inventory['files']:
        src, dst = BUNDLE / name, child(root, name)
        if not dst.exists() and not dst.is_symlink():
            copies.append(name)
        elif src.is_symlink() != dst.is_symlink() or digest(src) != digest(dst):
            errors.append(f'Existing source differs; preserve and reconcile it first: {name}')
    for asset in inventory['assets']:
        src = BUNDLE / asset['archive']
        if digest(src) != asset['compressed_sha256']:
            errors.append(f'Asset archive hash mismatch: {asset["archive"]}')
            continue
        dst = child(root, asset['destination'])
        current = digest(dst)
        if current is None:
            assets.append(asset)
        elif current != asset['sha256']:
            errors.append(f'Existing image differs: {asset["destination"]}')
    for patch in locks['patches']:
        src = BUNDLE / patch['patch']
        if digest(src) != patch['sha256']:
            errors.append(f'Patch hash mismatch: {patch["patch"]}')
            continue
        component = child(root, patch['path'])
        actual = [digest(child(root, patch['path'] + '/' + f['path'])) for f in patch['files']]
        before = [f['before'] for f in patch['files']]
        after = [f['after'] for f in patch['files']]
        if actual == after:
            continue
        if actual != before:
            errors.append(f'Patch inputs differ from pinned originals: {patch["path"]}')
            continue
        result = subprocess.run(['git', 'apply', '--check', str(src)], cwd=component,
                                capture_output=True, text=True)
        if result.returncode:
            errors.append(f'Patch check failed for {patch["path"]}: {result.stderr.strip()}')
        else:
            patches.append(patch)
    if errors:
        print('\n'.join(errors), file=sys.stderr)
        print('Preflight failed; no destination files changed.', file=sys.stderr)
        return 1
    summary = {'mode': 'apply' if args.apply else 'check', 'source_files_to_copy': len(copies),
               'images_to_restore': len(assets), 'patches_to_apply': len(patches),
               'android_root': str(root)}
    print(json.dumps(summary, ensure_ascii=False))
    if not args.apply:
        return 0
    for name in copies:
        src, dst = BUNDLE / name, child(root, name)
        dst.parent.mkdir(parents=True, exist_ok=True)
        if src.is_symlink():
            dst.symlink_to(os.readlink(src))
        else:
            shutil.copy2(src, dst)
    for asset in assets:
        dst = child(root, asset['destination'])
        dst.parent.mkdir(parents=True, exist_ok=True)
        tmp = dst.with_name(dst.name + '.ishtar-tmp')
        if tmp.exists():
            raise ValueError(f'Temporary output already exists: {tmp}')
        try:
            with gzip.open(BUNDLE / asset['archive'], 'rb') as src, tmp.open('xb') as target:
                shutil.copyfileobj(src, target)
            if digest(tmp) != asset['sha256'] or tmp.stat().st_size != asset['bytes']:
                raise ValueError(f'Decompressed image hash mismatch: {asset["destination"]}')
            tmp.rename(dst)
        finally:
            if tmp.exists():
                tmp.unlink()
    for patch in patches:
        subprocess.run(['git', 'apply', str(BUNDLE / patch['patch'])],
                       cwd=root / patch['path'], check=True)
        for f in patch['files']:
            if digest(root / patch['path'] / f['path']) != f['after']:
                raise ValueError(f'Applied patch hash mismatch: {patch["path"]}/{f["path"]}')
    print('Integration complete. This command does not compile or flash a phone.')
    return 0


if __name__ == '__main__':
    try:
        sys.exit(main())
    except (ValueError, OSError, subprocess.CalledProcessError) as error:
        print(str(error), file=sys.stderr)
        sys.exit(1)
