#!/bin/bash

mysql -u root < ./create+insert.sql
echo "Migration Completed!"