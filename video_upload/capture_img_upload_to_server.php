<?php

include 'dbConnect.php';

// Create connection
$conn = new mysqli($HostName, $HostUser, $HostPass, $DatabaseName);
 
 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
	 $DefaultId = 0;
 
 $ImageData = $_POST['image_path'];
 
 $ImageName = $_POST['image_name'];

 $GetOldIdSQL ="SELECT id FROM upload_image_video ORDER BY id ASC";
 
 $Query = mysqli_query($con,$GetOldIdSQL);
 
 while($row = mysqli_fetch_array($Query)){
 
 $DefaultId = $row['id'];
 }
 
 $ImagePath = "images/$DefaultId.png";
 
 $ServerURL = "http://192.168.43.223/video_upload/$ImagePath";
 
 $InsertSQL = "insert into upload_image_video (image,image_name) values ('$ServerURL','$ImageName')";
 
 if(mysqli_query($con, $InsertSQL)){

 file_put_contents($ImagePath,base64_decode($ImageData));

 echo "Your Image Has Been Uploaded.";
 }
 
 mysqli_close($con);
 }else{
 echo "Not Uploaded";
 }

?>