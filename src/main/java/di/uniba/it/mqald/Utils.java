/**
 * Copyright (c) 2020, the MQALD AUTHORS.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the University of Bari nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * GNU GENERAL PUBLIC LICENSE - Version 3, 29 June 2007
 *
 */

package di.uniba.it.mqald;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lucia
 */
public class Utils {

    public static List<String> checkFilters(String query) {
        List<String> modifiers = new ArrayList<>();

        //Query forms
        if (query.contains("ASK")) {
            modifiers.add("ASK");
        }
        if (query.contains("CONSTRUCT")) { //no
            modifiers.add("CONSTRUCT");
        }
        if (query.contains("DESCRIBE")) { //no
            modifiers.add("DESCRIBE");
        }

        //Patterns
        if (query.contains("BIND")) {
            modifiers.add("BIND");
        }
        if (query.contains("FILTER") || query.contains("filter")) {
            modifiers.add("FILTER");
        }
        if (query.contains("HAVING")) {
            modifiers.add("HAVING");
        }
        if (query.contains("GROUP BY")) {
            modifiers.add("GROUP BY");
        }
        if (query.contains("UNION")) {
            modifiers.add("UNION");
        }

        //Modifiers
        if (query.contains("LIMIT")) {
            modifiers.add("LIMIT");
        }
        if (query.contains("OFFSET")) {
            modifiers.add("OFFSET");
        }
        if (query.contains("ORDER BY")) {
            modifiers.add("ORDER BY");
        }

        //Aggregate functions
        if (query.contains("COUNT")) {
            modifiers.add("COUNT");
        }
        if (query.contains("SUM")) { //no
            modifiers.add("SUM");
        }
        if (query.contains("MIN")) { //no
            modifiers.add("MIN");
        }
        if (query.contains("MAX")) { //no
            modifiers.add("MAX");
        }
        if (query.contains("AVG")) { //no
            modifiers.add("AVG");
        }

        if (query.contains("NOW(") || query.contains("now(")) {
            modifiers.add("NOW");
        }
        if (query.contains("YEAR(") || query.contains("year(")) {
            modifiers.add("YEAR");
        }

        return modifiers;
    }

   
}
