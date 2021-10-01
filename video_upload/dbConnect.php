<?php
	define('HOST','192.168.43.223');
	define('USER','admin');
	define('PASS','admin');
	define('DB','android_php');
	
	$con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');


// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }else{  //echo "Connect"; 
  
   
   }
	?>