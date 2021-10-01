<?php
//error_reporting(0);
include_once("dbConnect.php");


$v1=$_REQUEST['f1'];
$v2=$_REQUEST['f2'];
$v3=$_REQUEST['f3'];
$v4=$_POST['f4'];

//$con=new mysqli($servername,$username,$password,$dbname);

$response = array();
 
if ($con->connect_error) {
 
 die("Connection failed: " . $con->connect_error);
} 

$sql="UPDATE upload_image_video SET user_name='$v1',user_email='$v2',frnd_email='$v3' WHERE user_name='$v4'";

$retrival = $con->query($sql);




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