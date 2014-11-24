<?php

/**
 * File to handle all API requests
 * Accepts GET and POST
 * 
 * Each request will be identified by TAG
 * Response will be JSON data
 * check for POST request 
 */

if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // get tag
    $tag = $_POST['tag'];

    // include db handler
    //require_once 'include/DB_Functions.php';
	include('DB_Functions.php');
    $db = new DB_Functions();

    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);

    // check for tag type
	switch ($tag) {
	case 'get_apps_names':
		$year = $_POST['year'];
		$semester = $_POST['semester'];
		$class = $_POST['class'];
		$apps = $db->getAppsNames($year, $semester, $class);
		if ($apps){
			$response["success"] = 1;
			$response["apps"] = $apps;
			echo json_encode($response);
		}else{
			$response["success"] = 0;
			$response["error"] = 2;
			echo json_encode($response);
		}
		break;

	case 'get_categories_names':
		$year = $_POST['year'];
		$semester = $_POST['semester'];
		$class = $_POST['class'];
		$catgs = $db->getCategoriesNames($year, $semester, $class);
		if ($catgs){
			$response["success"] = 1;
			$response["categories"] = $catgs;
			echo json_encode($response);
		}else{
			$response["success"] = 0;
			$response["error"] = 2;
			echo json_encode($response);
		}
		break;

	case 'get_apps_catgs':
		$year = $_POST['year'];
		$semester = $_POST['semester'];
		$class = $_POST['class'];
		
		$apps = $db->getAppsNames($year, $semester, $class);
		$catgs = $db->getCategoriesNames($year, $semester, $class);
		if ($apps && $catgs){
			$response["success"] = true;
			$response["apps"] = $apps;
			$response["categories"] = $catgs;
			echo json_encode($response);
		}else{
			$response["success"] = false;
			$response["error"] = 2;
			echo json_encode($response);
		}
		break;

	case 'vote_for_app':
		$year = $_POST['year'];
		$semester = $_POST['semester'];
		$class = $_POST['class'];
		$phoneID = $_POST['phoneID'];
		$votes = json_decode($_POST['votes'], true);
		$app_name = $_POST['app_name'];
		$counter = 0;
		foreach ($votes as $category => $grade) {
			if($db->voteAppCtg($year, $semester, $class, $phoneID, $app_name, $category, $grade))
				$counter += 1;
		}
		
		if ($counter > 0){
			$response["success"] = 1;
			$response["counter"] = $counter;
			echo json_encode($response);
		} else {
			$response["success"] = 0;
			$response["error"] = 2;
			echo json_encode($response);
		}
		break;

	case 'get_user_details':
		$user_name = $_POST['user_name'];
		$user = $db->getUserDetails($user_name);
		// var_dump($user);
		if ($user){
			$response["success"] = 1;
			// check if the user is artist 
			// the artist have a different rows
			if ($user[0] == "artist"){
				// separate the links from the artist details
				$response["artist_links"] = $user[6];
				unset($user[6]);
				$response["user_details"] = $user;
			} else
				$response["user_details"] = $user;
		}else{
			$response["success"] = 0;
			$response["error"] = 2;
		}
		echo json_encode($response);
		break;

	default:
		echo "Invalid Request";
		break;
	}
    
} else {
    echo "Access Denied";
}
?>