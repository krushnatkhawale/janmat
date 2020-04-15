sudo systemctl daemon-reload
sudo systemctl enable janmat-frontend.service
sudo systemctl start janmat-frontend
sudo systemctl status janmat-frontend -l