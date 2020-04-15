[Unit]
Description=Janmat frontend Application as a Service
[Service]
User=ec2-user
#change this directory into your workspace
#mkdir workspace 
WorkingDirectory=/opt/janamt
#path to the executable bash script which executes the jar file
ExecStart=/bin/bash /opt/janmat/start_app.sh
SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5
[Install]
WantedBy=multi-user.target