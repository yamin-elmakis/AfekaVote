<?php

define('VOTERS_COUNT',  "voters_count");
define('SUCCESS',       "success");
define('VOTED_APPS_COUNT',    "apps_count");
define('MIN_VOTERS',    "min_voters");
define('MUN_OF_APPS',   "num_apps");
define('VOTERS_COUNT',   "voters_count");


class DB_Functions {

    private $db;

    //put your code here
    // constructor
    function __construct() {     

    include('db_connect.php');
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() {   }
    
    /**
    * get the grade of each app in this contest
    */
    public function getAppsGrades($year, $semester, $class) {
        $result = mysql_query("SELECT app_name , sum(grade) as grade from app_votes WHERE year = '$year' AND class_name = '$class' AND semester = '$semester' GROUP BY app_name");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $appsGrades = array();
            while($res = mysql_fetch_assoc($result)) {
                $appsGrades[$res['app_name']] = $res['grade'];
            }
            return $appsGrades; 
        } else {
            return false;
        }
    }

    /**
    * get the number of voter in this specific contest
    */
    public function getAppsVotersCount($year, $semester, $class) {
        $result = mysql_query("SELECT count(distinct(app_name)) as ".VOTED_APPS_COUNT.", count(distinct(phone_id)) as ".VOTERS_COUNT." from app_votes WHERE year = '$year' AND class_name = '$class' AND semester = '$semester'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $res = mysql_fetch_assoc($result);
            return $res;  
        } else {
            return false;
        }
    }

     /**
     * get the min munber of voters in a specific class
     */
    public function getMinVoters($year, $semester, $class) {
        $result = mysql_query("SELECT min_votes from classes WHERE year = '$year' AND class_name = '$class' AND semester = '$semester'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $res = mysql_fetch_assoc($result);
            return $res['min_votes'];  
        } else {
            return false;
        }
    }

    /**
     * get the apps names in this contest
     */
    public function getAppsNames($year, $semester, $class) {
        $result = mysql_query("SELECT app_name from apps_names WHERE year = '$year' AND class_name = '$class' AND semester = '$semester' ");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // get apps     
            $apps = array();
            while($res = mysql_fetch_assoc($result)) {
                $apps[] = $res['app_name'];
            }
            return $apps;   
        } else {
            return false;
        }
    }

/**
     * get the apps names in this contest
     */
    public function getAppsCount($year, $semester, $class) {
        $result = mysql_query("SELECT count(app_name) as ".MUN_OF_APPS." from apps_names WHERE year = '$year' AND class_name = '$class' AND semester = '$semester' ");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $res = mysql_fetch_assoc($result);
            return $res[MUN_OF_APPS];    
        } else {
            return false;
        }
    }

    /**
     * vor for app and category
     */
    public function voteAppCtg($year, $semester, $class, $phoneID, $app_name, $category, $grade) {
        $query = "INSERT INTO app_votes VALUES ('$year','$semester','$class','$phoneID','$app_name','$category','$grade')
                ON DUPLICATE KEY UPDATE grade = '$grade'";
        $result = mysql_query($query);
        $no_of_rows = mysql_affected_rows();
        if ($no_of_rows > 0) {
            // insert or update ok     
            return true;   
        } else {
            return false;
        }
    }

     /**
     * get the categories names in this contest 
     */
    public function getCategoriesNames($year, $semester, $class) {
        $result = mysql_query("SELECT catg_name from catg_names WHERE year = '$year' AND class_name = '$class' AND semester = '$semester' ");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // get categories        
            $catgs = array();
            while($res = mysql_fetch_assoc($result)) {
                $catgs[] = $res['catg_name'];
            }
            return $catgs;
        } else {
            // user not existed
            return false;
        }
    }
}


?>