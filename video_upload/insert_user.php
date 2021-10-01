<?php
//error_reporting(0);
include_once("dbConnect.php");


$v1=$_REQUEST['f1'];
$v2=$_REQUEST['f2'];
$v3=$_REQUEST['f3'];
$v4=$_REQUEST['f4'];
$v5=$_REQUEST['f5'];

//$con=new mysqli($servername,$username,$password,$dbname);

$response = array();
 
if ($con->connect_error) {
 
 die("Connection failed: " . $con->connect_error);
} 

$sql1="insert into upload_image_video(latitude,longitude,user_name,user_email,frnd_email) VALUES ('$v4','$v5','$v1','$v2','$v3')";
$retrival = $con->query($sql1);
if($retrival)
{
    $response["success"] = 1;
    $response["message"] = "Inserted successfully.";
    echo json_encode($response);
    
}
else
{
    $response["success"] = 0;
    $response["message"] = "Insertion failed.";
    echo json_encode($response);
}


$con->close();
?>