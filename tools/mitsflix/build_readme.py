#!/usr/bin/env python3
"""Build README.md from tools/mitsflix/README.template.md.

The feature list comes from OwnTV's README for the release being merged, re-branded, so the Mitsflix
README always lists every current feature:
    python3 tools/mitsflix/build_readme.py path/to/owntv-README.md
With no argument (or if OwnTV's README has no Features section) the list already in README.md is kept.
"""
import os
import re
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
TEMPLATE = os.path.join(ROOT, "tools/mitsflix/README.template.md")
README = os.path.join(ROOT, "README.md")
HEADING = re.compile(r"^## .*Features\s*$", re.M)


def features_from(text: str) -> str | None:
    m = HEADING.search(text)
    if not m:
        return None
    rest = text[m.end():]
    end = re.search(r"^(---|## )", rest, re.M)
    block = (rest[: end.start()] if end else rest).strip()
    return block or None


def rebrand(block: str) -> str:
    block = re.sub(r"\ban OwnTV\b", "a Mitsflix Player", block)
    block = re.sub(r"\bAn OwnTV\b", "A Mitsflix Player", block)
    block = block.replace("OwnTV", "Mitsflix Player")
    # OwnTV's community/translation links aren't Mitsflix's.
    lines = [l for l in block.splitlines() if not re.search(r"t\.me/|weblate|telegram", l, re.I)]
    return "\n".join(lines).strip()


def main() -> None:
    features = None
    if len(sys.argv) > 1 and os.path.isfile(sys.argv[1]):
        features = features_from(open(sys.argv[1], encoding="utf-8").read())
        if features:
            features = rebrand(features)
    if not features and os.path.isfile(README):
        features = features_from(open(README, encoding="utf-8").read())
    if not features:
        sys.exit("No feature list found in OwnTV's README or the current README.")
    out = open(TEMPLATE, encoding="utf-8").read().replace("{{FEATURES}}", features)
    open(README, "w", encoding="utf-8").write(out)
    print(f"README.md written ({features.count(chr(10)) + 1} feature lines).")


if __name__ == "__main__":
    main()
