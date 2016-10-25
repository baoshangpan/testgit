#!/usr/bin/expect -f 

set port 22  
set user gongzuozhan  
set password 123456
set timeout -1  
set path /home/gongzuozhan/Desktop
set folder /home/gongzuozhan/Desktop/scps 
  
set hostarray(1) 192.168.88.130  
set hostarray(2) 192.168.88.136
set hostarray(3) 192.168.88.131

for {set i 1} {$i < 4} {incr i} {
	set host $hostarray($i)
	spawn scp -P $port -r $folder $user@$host:$path  
	expect {
	"*yes/no*" {
		send "yes\r" }
	"*assword:*" {
		send "$password\r" } 
	"*No route to host*" { 
		puts "continue"
		continue }
	}
	expect eof
} 


