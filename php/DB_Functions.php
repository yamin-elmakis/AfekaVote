<?php

class DB_Functions {

    private $db;

    //put your code here
    // constructor
    function __construct() {     
    // require_once 'DB_Connect.php';
    include('db_connect.php');
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() {   }
     
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


    /**
     * Storing new song counter
     * returns song details
     */
    public function setNewSongCounter($id,$counter) {

        $result = mysql_query("INSERT INTO song_counter(id,counter) VALUES('$id','$counter')");
        // check for successful store
        if ($result) {
            // get song counter details 
          //  $id = mysql_insert_id(); // last inserted id
        //  echo $id ;
            $result = mysql_query("SELECT * FROM song_counter WHERE id = '$id' ");
            // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }
    
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
        return $hash;
    }
}


?>