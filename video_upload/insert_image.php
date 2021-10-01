<?php
 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
	$image = $_POST['image'];
	$v1=$_POST['f1'];
	$v2=$_POST['f2'];
	$v3=$_POST['f3'];
	$v4=$_POST['f4'];
	$v5=$_POST['f5'];

		
		require_once('dbConnect.php');
		$sql ="SELECT id FROM add_news ORDER BY id ASC";
		
		$res = mysqli_query($con,$sql);
		
		$id = 0;
		
		while($row = mysqli_fetch_array($res)){
				$id = $row['id'];
		}
		
		$path = "uploadedImages/$id.jpg";
		
		
		
		$actualpath = "http://192.168.43.223/video_upload/$path";

		$sql = "INSERT INTO upload_image_video(image,latitude,longitude,user_name,user_email,frnd_email) VALUES ('$actualpath','$v1','$v2','$v3','$v4','$v5')";
		
		if(mysqli_query($con,$sql)){
			file_put_contents($path,base64_decode($image));
			echo "Successfully Uploaded";
		}
		mysqli_close($con);
	}else{
		echo "Error";
	}
	
	
	
	
	?>