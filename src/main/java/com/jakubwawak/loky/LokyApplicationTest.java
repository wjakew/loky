/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package com.jakubwawak.loky;

/**
 * LokyApplicationTest is a test class for the LokyApplication class.
 */
public class LokyApplicationTest {

    /**
     * Constructor for LokyApplicationTest
     */
    public LokyApplicationTest(){
        run();
    }

    /**
     * Run the test
     */
    public void run(){
        String cookie = LokyApplication.database.createAdminCookie();
        System.out.println(cookie);
        System.out.println(LokyApplication.database.verifyAdminCookie(cookie));
    }
    
}
