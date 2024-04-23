#!/bin/bash -e

influx_data_dir="/mnt/influx_data"
if [ ! -d "$influx_data_dir" ]; then
	echo "No influx_data folder, creating it."
	sudo mkdir -p "$influx_data_dir"
	if [ $? -eq 0 ]; then
		echo "influx_data folder creation successful"
		sudo chown -R influxdb:influxdb "$influx_data_dir"
	else
		echo "influx_data folder creation failed"
	fi
else
	echo "influx_data directory already exists"
fi 

/usr/bin/influxd --bolt-path=/mnt/influx_data/.influxdbv2/influxd.bolt --engine-path=/mnt/influx_data/.influxdbv2/engine $INFLUXD_OPTS &
PID=$!
echo $PID >/var/lib/influxdb/influxd.pid

PROTOCOL="http"
BIND_ADDRESS=$(influxd print-config --key-name http-bind-address)
TLS_CERT=$(influxd print-config --key-name tls-cert | tr -d '"')
TLS_KEY=$(influxd print-config --key-name tls-key | tr -d '"')
if [ -n "${TLS_CERT}" ] && [ -n "${TLS_KEY}" ]; then
  echo "TLS cert and key found -- using https"
  PROTOCOL="https"
fi
HOST=${BIND_ADDRESS%:*}
HOST=${HOST:-"localhost"}
PORT=${BIND_ADDRESS##*:}

set +e
attempts=0
url="$PROTOCOL://$HOST:$PORT/ready"
result=$(curl -k -s -o /dev/null $url -w %{http_code})
while [ "${result:0:2}" != "20" ] && [ "${result:0:2}" != "40" ]; do
  attempts=$(($attempts + 1))
  echo "InfluxDB API at $url unavailable after $attempts attempts..."
  sleep 1
  result=$(curl -k -s -o /dev/null $url -w %{http_code})
done
echo "InfluxDB started"
set -e