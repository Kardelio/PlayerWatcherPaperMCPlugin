#!/usr/bin/env bash

if [ -z "$1" ]; then
    echo "Usage: $0 <input_file>"
    exit 1
fi

INPUT_FILE="$1"

# Check if the file exists.
if [ ! -f "$INPUT_FILE" ]; then
    echo "Error: File '$INPUT_FILE' not found!"
    exit 1
fi

#regex='^.*\/(.*) - ([0-9]{4}-[0-9]{2}-[0-9]{2})\..*$'
regex='^(\".*\")\:\ \"(.*)\",?$'
threeargs='.*%3\$s.*'
twoargs='%2\$s'
onearg='%1\$s'


cat "$INPUT_FILE" | while read -r line; do
# Skip empty lines and lines that don't match the expected pattern.
    if [[ "$line" =~ $regex ]]; then
        key="${BASH_REMATCH[1]}"
        contents="${BASH_REMATCH[2]}"
        count=0
        if [[ "$contents" =~ $threeargs ]]; then
            count=3
        elif [[ "$contents" =~ $twoargs ]]; then
            count=2
        elif [[ "$contents" =~ $onearg ]]; then
            count=1
        else
            count=0
        fi
        echo "${key} to ${count},"
    fi
done
