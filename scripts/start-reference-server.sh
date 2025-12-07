#!/bin/bash
# Start the reference server

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BASE_DIR="$(dirname "$SCRIPT_DIR")"
java -cp "$BASE_DIR/.libs/reference-server-0.1.0" za.co.wethinkcode.robots.server.Server