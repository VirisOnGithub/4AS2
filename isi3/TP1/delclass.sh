#!/bin/bash
find "$(dirname "$0")" -type f -name "*.class" -exec rm -v {} \;