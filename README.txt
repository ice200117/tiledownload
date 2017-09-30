1.copy files to remote server
	scp -r /map/gmapoffline/ root@45.32.43.235:~/
2.install java
	run ./install_java8_in_debian.command
3.start download
	run ./start_download.command
4.start zip
	run ./start_zip.command
5.copy files from remote server
	scp -r root@45.32.43.235:~/gmapoffline/ /map/
