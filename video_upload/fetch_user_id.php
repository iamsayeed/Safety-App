<?php
include_once("dbConnect.php");
 $CategoryName= $_POST['f_id'];
// Create connection
//$conn = new mysqli($HostName, $HostUser, $HostPass, $DatabaseName);

 
if ($con->connect_error) {
 
 die("Connection failed: " . $con->connect_error);
} 

$sql = "SELECT * FROM upload_image_video where user_name = '$CategoryName'" ;

$result = $con->query($sql);

if ($result->num_rows >0) {
 
 
 while($row[] = $result->fetch_assoc()) {
 
 $tem = $row;
 
 $json = json_encode($tem);
 
 
 }
 
} else {
 echo "No Results Found.";
}
 echo $json;
$con->close();
?>