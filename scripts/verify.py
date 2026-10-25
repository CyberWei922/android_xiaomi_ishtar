#!/usr/bin/env python3
"""Verify the exported bundle against SHA256SUMS.json."""
from pathlib import Path
import hashlib
import json
import os
import sys

root = Path(__file__).resolve().parents[1]
records = json.loads((root / 'SHA256SUMS.json').read_text())
errors = []
for name, expected in records.items():
    p = root / name
    if not p.exists() and not p.is_symlink():
        errors.append(name + ': missing')
        continue
    data = os.fsencode(os.readlink(p)) if p.is_symlink() else p.read_bytes()
    if hashlib.sha256(data).hexdigest() != expected:
        errors.append(name + ': hash mismatch')
if errors:
    print('\n'.join(errors), file=sys.stderr)
    sys.exit(1)
print(f'Verified {len(records)} files.')
