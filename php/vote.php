<?php

/**
 * File to handle all API requests
 * Accepts POST requests
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
	
	case 'get_apps_grades':
		$year = $_POST['year'];
		$semester = $_POST['semester'];
		$class = $_POST['class'];
		$apps_grades = $db->getAppsGrades($year, $semester, $class);
		if ($apps_grades){
			$response["success"] = true;
			$response['apps_grades'] = $apps_grades;
			echo json_encode($response);
		}else{
			$response["success"] = false;
			$response["error"] = 2;
			echo json_encode($response);
		}
		break;

	case 'check_status':
		$year = $_POST['year'];
		$semester = $_POST['semester'];
		$class = $_POST['class'];
		$num_apps_voters = false;
		$min_voters = false;

		$num_apps = $db->getAppsCount($year, $semester, $class);
		if ($num_apps){
			$num_apps_voters = $db->getAppsVotersCount($year, $semester, $class);
		}
		if ($num_apps && $num_apps_voters){
			$min_voters = $db->getMinVoters($year, $semester, $class);
		}
		// if we got all the data then build an answer
		if ($num_apps && $num_apps_voters && $min_voters){
			$apps_counter = $num_apps_voters[VOTED_APPS_COUNT];
			$voters_count = $num_apps_voters[VOTERS_COUNT];
			$isOK = true;
			// check if each app have at least 1 vote
			// vote != points. 0 it's a vote too.
			if ($apps_counter != $num_apps)
				$isOK = false;
				
			// check if anough votes voted yet
			if ($voters_count < $min_voters)
				$isOK = false;

			// $response["apps_counter"] = $apps_counter;
			// $response["voters_count"] = $voters_count;
			// $response["num_apps"] = $num_apps;
			// $response["min_voters"] = $min_voters;

			if ($isOK){
				$response[SUCCESS] = true;
				$response[VOTERS_COUNT] = $voters_count;
			}else{
				$response[SUCCESS] = false;
				$response["error"] = 3;
			}
			echo json_encode($response);
		}else{
			$response[SUCCESS] = false;
			$response["error"] = 2;
			echo json_encode($response);
		}
		break;

	case 'get_min_voters':
		$year = $_POST['year'];
		$semester = $_POST['semester'];
		$class = $_POST['class'];
		$min_voters = $db->getMinVoters($year, $semester, $class);
		if ($min_voters){
			$response["success"] = true;
			$response["min_voters"] = $min_voters;
			echo json_encode($response);
		}else{
			$response["success"] = false;
			$response["error"] = 2;
			echo json_encode($response);
		}
		break;

	case 'get_apps_names':
		$year = $_POST['year'];
		$semester = $_POST['semester'];
		$class = $_POST['class'];
		$apps = $db->getAppsNames($year, $semester, $class);
		if ($apps){
			$response["success"] = true;
			$response["apps"] = $apps;
			echo json_encode($response);
		}else{
			$response["success"] = false;
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
			$response["success"] = true;
			$response["categories"] = $catgs;
			echo json_encode($response);
		}else{
			$response["success"] = false;
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
			$response["success"] = true;
			$response["counter"] = $counter;
			echo json_encode($response);
		} else {
			$response["success"] = false;
			$response["error"] = 2;
			echo json_encode($response);
		}
		break;

	case 'get_user_details':
		$user_name = $_POST['user_name'];
		$user = $db->getUserDetails($user_name);
		// var_dump($user);
		if ($user){
			$response["success"] = true;
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
			$response["success"] = false;
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