<?php

	include_once("dbConnect.php");
 
 $con = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);
 

 $lat1 = $_POST['lat1'];
 $long1 = $_POST['long1'];
 $add1 = $_POST['add'];

 


 $Sql_Query = "insert into upload_image_video (latitude,longitude,location_address) values ('$lat1','$long1','$add1')";
 
 if(mysqli_query($con,$Sql_Query)){
 
 echo 'Data Submit Successfully';
 
 }
 else{
 
 echo 'Try Again';
 
 }
 mysqli_close($con);
?>