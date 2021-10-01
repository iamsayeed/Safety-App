<?php
 if($_SERVER['REQUEST_METHOD']=='POST'){
  	// echo $_SERVER["DOCUMENT_ROOT"];  // /home1/demonuts/public_html
	//including the database connection file
  	include_once("dbConnect.php");

  	//$_FILES['image']['name']   give original name from parameter where 'image' == parametername eg. city.jpg
  	//$_FILES['image']['tmp_name']  temporary system generated name
  
        $originalImgName= $_FILES['filename']['name'];
		$v1=$_POST['f1'];
		$v2=$_POST['f2'];
		$v3=$_POST['f3'];
		$v4=$_POST['f4'];
		$v5=$_POST['f5'];
        $tempName= $_FILES['filename']['tmp_name'];
        $folder="uploadedFiles/";
        $url = "http://192.168.43.223/video_upload/uploadedFiles/".$originalImgName; //update path as per your directory structure 
        
        if(move_uploaded_file($tempName,$folder.$originalImgName)){
                $query = "INSERT INTO upload_image_video (pathToFile,longitude,latitude,user_name,user_email,frnd_email) VALUES ('$url','$v1','$v2','$v3','$v4','$v5')";
				//$query = "UPDATE upload_image_video set pathToFile='$url' where user_name='$v1'";
				
                if(mysqli_query($con,$query)){
                
                	 $query= "SELECT * FROM upload_image_video WHERE pathToFile='$url'";
	                 $result= mysqli_query($con, $query);
	                 $emparray = array();
	                     if(mysqli_num_rows($result) > 0){  
	                     while ($row = mysqli_fetch_assoc($result)) {
                                     $emparray[] = $row;
                                   }
                                   echo json_encode(array( "status" => "true","message" => "Success! file added" , "data" => $emparray) );
                                   
	                     }else{
	                     		echo json_encode(array( "status" => "false","message" => "Failed!") );
	                     }
			   
                }else{
                	echo json_encode(array( "status" => "false","message" => "Failed!") );
                }
        	//echo "moved to ".$url;
        }else{
        	echo json_encode(array( "status" => "false","message" => "Failed!") );
        }
  }
?>