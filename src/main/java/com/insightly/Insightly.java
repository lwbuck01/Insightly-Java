package com.insightly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

public class Insightly{
    public Insightly(String apikey){
        this.apikey = apikey;
    }

    public JSONObject addContact(JSONObject contact) throws IOException{
        try{
            if(contact.has("CONTACT_ID") && (contact.getLong("CONTACT_ID") > 0)){
                return verifyResponse(generateRequest("/v2.1/Contacts", "PUT", contact.toString()).asJson()).getBody().getObject();
            }
            else{
                return verifyResponse(generateRequest("/v2.1/Contacts", "POST", contact.toString()).asJson()).getBody().getObject();
            }
        }
        catch(UnirestException ex){
            throw new IOException(ex.getMessage());
        }
    }

    public void deleteContact(long contact_id) throws IOException{
        try{
            verifyResponse(generateRequest("/v2.1/Contacts/" + contact_id, "DELETE", "").asJson());
        }
        catch(UnirestException ex){
            throw new IOException(ex.getMessage());
        }
    }

    public JSONArray getContacts(Map<String, Object> options) throws IOException{
        try{
            List<String> query_strings = new ArrayList<String>();

            if(options.containsKey("email") && (options.get("email") != null)){
                String email = (String)options.get("email");
                query_strings.add("email=" + email);
            }
            if(options.containsKey("tag") && (options.get("tag") != null)){
                String tag = (String)options.get("tag");
                query_strings.add("tag=" + tag);
            }
            if(options.containsKey("ids") && (options.get("ids") != null)){
                List<Long> ids = (List<Long>)options.get("ids");
                if(ids.size() > 0){
                    StringBuilder acc = new StringBuilder();
                    for(Long id : ids){
                        acc.append(id);
                        acc.append(",");
                    }

                    query_strings.add("ids=" + acc);
                }
            }

            StringBuilder query_string = new StringBuilder();
            for(String s : query_strings){
                query_string.append(s);
                query_string.append("&");
            }

            return verifyResponse(generateRequest("/v2.1/Contacts" + query_string, "GET", "").asJson()).getBody().getArray();
        }
        catch(UnirestException ex){
            throw new IOException(ex.getMessage());
        }
    }

    public JSONArray getContactEmails(long contact_id) throws IOException{
        try{
            return verifyResponse(generateRequest("/v2.1/Contacts/" + contact_id + "/Emails", "GET", "").asJson()).getBody().getArray();
        }
        catch(UnirestException ex){
            throw new IOException(ex.getMessage());
        }
    }

    public JSONArray getContactNotes(long contact_id) throws IOException{
        try{
            return verifyResponse(generateRequest("/v2.1/Contacts/" + contact_id + "/Notes", "GET", "").asJson()).getBody().getArray();
        }
        catch(UnirestException ex){
            throw new IOException(ex.getMessage());
        }
    }

    public JSONArray getContactTasks(long contact_id) throws IOException{
        try{
            return verifyResponse(generateRequest("/v2.1/Contacts/" + contact_id + "/Tasks", "GET", "").asJson()).getBody().getArray();
        }
        catch(UnirestException ex){
            throw new IOException(ex.getMessage());
        }
    }

    public JSONArray getCountries() throws IOException{
        try{
            return verifyResponse(generateRequest("/v2.1/Countries", "GET", "").asJson()).getBody().getArray();
        }
        catch(UnirestException ex){
            throw new IOException(ex.getMessage());
        }
    }

    public JSONArray getCurrencies() throws IOException{
        try{
            return verifyResponse(generateRequest("/v2.1/Currencies", "GET", "").asJson()).getBody().getArray();
        }
        catch(UnirestException ex){
            throw new IOException(ex.getMessage());
        }
    }

    public JSONArray getCustomFields() throws IOException{
        try{
            return verifyResponse(generateRequest("/v2.1/CustomFields", "GET", "").asJson()).getBody().getArray();
        }
        catch(UnirestException ex){
            throw new IOException(ex.getMessage());
        }
    }

    public JSONArray getEmails(Map<String, Object> options) throws IOException{
        try{
            String query_string = buildQueryString(buildODataQuery(options));
            return verifyResponse(generateRequest("/v2.1/Emails" + query_string, "GET", "").asJson()).getBody().getArray();
        }
        catch(UnirestException ex){
            throw new IOException(ex.getMessage());
        }
    }

    public JSONObject getEvent(long id) throws IOException{
        return InsightlyRequest.GET(apikey, "/v2.1/Events/" + id).asJSONObject();
    }

    public JSONArray getEvents() throws IOException{
        return getEvents(null);
    }

    public JSONArray getEvents(Map<String, Object> options) throws IOException{
        InsightlyRequest request = InsightlyRequest.GET(apikey, "/v2.1/Events");
        if(options != null){
            buildODataQuery(request, options);
        }
        return request.asJSONArray();
    }

    public JSONObject addEvent(JSONObject event) throws IOException{
        InsightlyRequest request = null;
        if(event.has("EVENT_ID") && (event.getLong("EVENT_ID") > 0)){
            long event_id = event.getLong("EVENT_ID");
            request = InsightlyRequest.PUT(apikey, "/v2.1/Events");
        }
        else{
            request = InsightlyRequest.POST(apikey, "/v2.1/Events");
        }

        return request.body(event).asJSONObject();
    }

    public void deleteEvent(long id) throws IOException{
        InsightlyRequest.DELETE(apikey, "/v2.1/Events/" + id).asString();
    }

    public JSONArray getFileCategories() throws IOException{
        return InsightlyRequest.GET(apikey, "/v2.1/FileCategories").asJSONArray();
    }

    public JSONObject getFileCategory(long id) throws IOException{
        return InsightlyRequest.GET(apikey, "/v2.1/FileCategories/" + id).asJSONObject();
    }

    public JSONObject addFileCategory(JSONObject category) throws IOException{
        InsightlyRequest request = null;
        if(category.has("CATEGORY_ID") && (category.getLong("CATEGORY_ID") > 0)){
            request = InsightlyRequest.PUT(apikey, "/v2.1/FileCategories");
        }
        else{
            request = InsightlyRequest.POST(apikey, "/v2.1/FileCategories");
        }

        return request.body(category).asJSONObject();
    }

    public void deleteFileCategory(long id) throws IOException{
        InsightlyRequest.DELETE(apikey, "/v2.1/FileCategories/" + id);
    }

    public JSONArray getNotes() throws IOException{
        return this.getNotes(null);
    }

    public JSONArray getNotes(Map<String, Object> options) throws IOException{
        InsightlyRequest request = InsightlyRequest.GET(apikey, "/v2.1/Notes");

        if(options != null){
            buildODataQuery(request, options);
        }

        return request.asJSONArray();
    }

    public JSONObject getNote(long id) throws IOException{
        return InsightlyRequest.GET(apikey, "/v2.1/Notes/" + id).asJSONObject();
    }

    public JSONObject addNote(JSONObject note) throws IOException{
        InsightlyRequest request = null;

        if(note.has("NOTE_ID") && (note.getLong("NOTE_ID") > 0)){
            request = InsightlyRequest.PUT(apikey, "/v2.1/Notes");
        }
        else{
            request = InsightlyRequest.POST(apikey, "/v2.1/Notes");
        }

        return request.body(note).asJSONObject();
    }

    public JSONArray getNoteComments(long note_id) throws IOException{
        return InsightlyRequest.GET(apikey, "/v2.1/Notes/" + note_id + "/Comments").asJSONArray();
    }

    public JSONObject addNoteComment(long note_id, JSONObject comment) throws IOException{
        String url_path = "/v2.1/Notes/" + note_id + "/Comments";
        return InsightlyRequest.POST(apikey, url_path).body(comment).asJSONObject();
    }

    public void deleteNote(long id) throws IOException{
        InsightlyRequest.DELETE(apikey, "/v2.1/Notes/" + id).asString();
    }

    public JSONArray getUsers() throws IOException{
        try{
            return verifyResponse(generateRequest("/v2.1/Users", "GET", "").asJson()).getBody().getArray();
        }
        catch(UnirestException ex){
            throw new IOException(ex.getMessage());
        }
    }

    private InsightlyRequest buildODataQuery(InsightlyRequest request, Map<String, Object> options){
        if(options.containsKey("top") && (options.get("top") != null)){
            long top = ((Number)options.get("top")).longValue();
            if(top > 0){
                request.top(top);
            }
        }
        if(options.containsKey("skip") && (options.get("skip") != null)){
            long skip = ((Number)options.get("skip")).longValue();
            if(skip > 0){
                request.skip(skip);
            }
        }
        if(options.containsKey("orderby") && (options.get("orderby") != null)){
            String orderby = (String)options.get("orderby");
            request.orderBy(orderby);
        }
        if(options.containsKey("filters") && (options.get("filters") != null)){
            List<String> filters = (List<String>)options.get("filters");
            request.filter(filters);
        }

        return request;
    }

    protected List<String> buildODataQuery(Map<String, Object> options){
        List<String> query_strings = new ArrayList<String>();
        if(options.containsKey("top") && (options.get("top") != null)){
            long top = ((Number)options.get("top")).longValue();
            if(top > 0){
                query_strings.add("$top=" + top);
            }
        }
        if(options.containsKey("skip") && (options.get("skip") != null)){
            long skip = ((Number)options.get("skip")).longValue();
            if(skip > 0){
                query_strings.add("$skip=" + skip);
            }
        }
        if(options.containsKey("orderby") && (options.get("orderby") != null)){
            String orderby = (String)options.get("orderby");
            // TODO:  encode orderby
            query_strings.add("$orderby=" + orderby);
        }
        if(options.containsKey("filters") && (options.get("filters") != null)){
            List<String> filters = (List<String>)options.get("filters");
            for(String f : filters){
                // TODO: encode f
                query_strings.add("$filter=" + f);
            }
        }

        return query_strings;
    }

    protected String buildQueryString(List<String> query_strings){
        StringBuilder query_string = new StringBuilder();
        if(query_strings.size() > 0){
            boolean first = true;
            for(String s : query_strings){
                if(!first){
                    query_string.append("&");
                    first = false;
                }

                query_string.append(s);
            }
        }

        return query_string.toString();
    }

    public HttpRequest generateRequest(String url, String method, String data) throws IOException{
        try{
            HttpRequest request = null;
            url = BASE_URL + url;

            if(method.equals("GET")){
                request = Unirest.get(url);
            }
            else if(method.equals("PUT")){
                HttpRequestWithBody req = Unirest.put(url);
                req.header("Content-Type", "application/json");
                req.body(data);

                request = req;
            }
            else if(method.equals("DELETE")){
                request = Unirest.delete(url);
            }
            else if(method.equals("POST")){
                HttpRequestWithBody req = Unirest.post(url);
                req.header("Content-Type", "application/json");
                req.body(data);

                request = req;
            }
            else{
                throw new IOException("parameter method must be GET|DELETE|PUT|UPDATE");
            }

            request.basicAuth(apikey, "");

            return request;
        }
        catch(IOException ex){
            throw ex;
        }
    }

    protected <T> HttpResponse<T> verifyResponse(HttpResponse<T> response) throws IOException{
        int status_code = response.getCode();
        if(!(status_code == 200
             || status_code == 201
             || status_code == 202)){
            throw new IOException("Server returned status code " + response.getCode());
        }

        return response;
    }

    public static void main(String[] args) throws Exception{
        if(args.length < 1){
            System.err.println("Please provide your api key as a command-line argument");
            System.exit(1);
        }

        Insightly insightly = new Insightly(args[0]);
        insightly.test();
        System.out.println("Tests complete!");
        System.exit(0);
    }

    public void test() throws Exception{
        test(-1);
    }

    public void test(int top) throws Exception{
        System.out.println("Testing API .....");

        int passed = 0;
        int failed = 0;

        Map<String, Object> options;

        System.out.println("Testing authentication");
        JSONArray currencies = this.getCurrencies();
        if(currencies.length() > 0){
            System.out.println("Authentication passed");
            passed += 1;
        }
        else{
            failed += 1;
        }

        // Test getUsers()
        // also get root user to use in testing write/update calls
        JSONArray users = null;
        JSONObject user = null;
        long user_id = 0;
        try{
            users = this.getUsers();
            user = users.getJSONObject(0);
            user_id = user.getLong("USER_ID");
            System.out.println("PASS: getUsers(), found " + users.length() + " users.");
            passed += 1;
        }
        catch(Exception ex){
            System.out.println("FAIL: getUsers()");
            failed += 1;
        }

        // getContacts
        JSONObject contact = null;
        try{
            JSONArray contacts;
            options = new HashMap<String, Object>();
            options.put("order_by", "DATE_UPDATED_UTL desc");
            options.put("top", top);
            contacts = this.getContacts(options);
            contact = contacts.getJSONObject(0);
            System.out.println("PASS: getContacts(), found " + contacts.length() + " contacts.");
            passed += 1;
        }
        catch(Exception e){
            System.out.println("FAIL: getContacts()");
            failed += 1;
        }

        if(contact != null){
            long contact_id = contact.getLong("CONTACT_ID");
            try{
                JSONArray emails = this.getContactEmails(contact_id);
                System.out.println("PASS: getContactEmails(), found " + emails.length() + " emails for random contact.");
                passed += 1;
            }
            catch(Exception ex){
                System.out.println("FAIL: getContactEmails()");
                failed += 1;
            }

            try{
                JSONArray notes = this.getContactNotes(contact_id);
                System.out.println("PASS: getContactNotes(), found " + notes.length() + " notes for random contact.");
                passed += 1;
            }
            catch(Exception ex){
                System.out.println("FAIL: getContactNotes()");
                failed += 1;
            }

            try{
                JSONArray tasks = this.getContactTasks(contact_id);
                System.out.println("PASS: getContactTasks(), found " + tasks.length() + " tasks for random contact.");
                passed += 1;
            }
            catch(Exception ex){
                System.out.println("FAIL: getContactTasks()");
                failed += 1;
            }
        }

        // Test addContact
        try{
            contact = new JSONObject();
            contact.put("SALUTATION", "Mr");
            contact.put("FIRST_NAME", "Testy");
            contact.put("LAST_NAME", "McTesterson");

            contact = this.addContact(contact);
            System.out.println("PASS: addContact()");
            passed += 1;

            try{
                this.deleteContact(contact.getLong("CONTACT_ID"));
                System.out.println("PASS: deleteContact()");
                passed += 1;
            }
            catch(Exception ex){
                System.out.println("FAIL: deleteContact()");
                failed += 1;
            }
        }
        catch(Exception ex){
            contact = null;
            System.out.println("FAIL: addContact()");
            failed += 1;
        }

        // Test getCountries()
        try{
            JSONArray countries = this.getCountries();
            System.out.println("PASS: getCountries(), found " + countries.length() + " countries.");
            passed += 1;
        }
        catch(Exception ex){
            System.out.println("FAIL: getCountries");
            failed += 1;
        }

        // Test getCurrencies()
        try{
            currencies = this.getCurrencies();
            System.out.println("PASS: getCurrencies(), found " + currencies.length() + " currencies.");
            passed += 1;
        }
        catch(Exception ex){
            System.out.println("FAIL: getCurrencies()");
            failed += 1;
        }

        // Test getCustomFields()
        try{
            JSONArray custom_fields = this.getCustomFields();
            System.out.println("PASS: getCustomFields(), found " + custom_fields.length() + " custom fields.");
            passed += 1;
        }
        catch(Exception ex){
            System.out.println("FAIL: getCustomFields()");
            failed += 1;
        }

        // Test getEmails()
        try{
            options = new HashMap<String, Object>();
            options.put("top", top);
            JSONArray emails = this.getEmails(options);
            System.out.println("PASS: getEmails(), found " + emails.length() + " emails.");
            passed += 1;
        }
        catch(Exception ex){
            System.out.println("FAIL: getEmails()");
            failed += 1;
        }

        // TODO:  Test getEmail()

        // Test getEvents()
        try{
            options = new HashMap<String, Object>();
            options.put("top", top);
            JSONArray events = this.getEvents(options);
            System.out.println("PASS: getEvents(), found " + events.length() + " events.");
            passed += 1;
        }
        catch(Exception ex){
            System.out.println("FAIL: getEvents()");
            failed += 1;
        }

        // Test addEvent()
        JSONObject event = null;
        try{
            JSONObject new_event = new JSONObject();
            new_event.put("TITLE", "Text Event");
            new_event.put("LOCATION", "Somewhere");
            new_event.put("DETAILS", "Details");
            new_event.put("START_DATE_UTC", "2014-07-12 12:00:00");
            new_event.put("END_DATE_UTC", "2014-07-12 13:00:00");
            new_event.put("OWNER_USER_ID", user_id);
            new_event.put("ALL_DAY", false);
            new_event.put("PUBLICLY_VISIBLE", true);
            event = this.addEvent(new_event);
            System.out.println("PASS: addEvent()");
            passed += 1;
        }
        catch(Exception ex){
            System.out.println("FAIL: addEvent()");
            ex.printStackTrace();
            failed += 1;
        }

        // Test deleteEvent()
        if(event != null){
            try{
                this.deleteEvent(event.getLong("EVENT_ID"));
                System.out.println("PASS: deleteEvent()");
                passed += 1;
            }
            catch(Exception ex){
                System.out.println("FAIL: deleteEvent()");
                failed += 1;
            }
        }

        // Test getFileCategories()
        try{
            JSONArray categories = this.getFileCategories();
            System.out.println("PASS: getFileCategories(), found " + categories.length() + " file categories.");
            passed += 1;
        }
        catch(Exception ex){
            System.out.println("FAIL: getFileCategories()");
            failed += 1;
        }

        // Test addFileCategory()
        JSONObject category = null;
        try{
            category = new JSONObject();
            category.put("CATEGORY_NAME", "Test Category");
            category.put("ACTIVE", true);
            category.put("BACKGROUND_COLOR", "000000");
            category = this.addFileCategory(category);
            System.out.println("PASS: addFileCategory()");
            passed += 1;
        }
        catch(Exception ex){
            category = null;
            System.out.println("FAIL: addFileCategory()");
            failed += 1;
        }

        // Test deleteFileCategory()
        try{
            if(category != null){
                this.deleteFileCategory(category.getLong("CATEGORY_ID"));
                System.out.println("PASS: deleteFileCategory()");
                passed += 1;
            }
        }
        catch(Exception ex){
            System.out.println("FAIL: deleteFileCategory()");
            failed += 1;
        }

        try{
            JSONArray notes = this.getNotes();
            System.out.println("PASS: getNotes()");
            passed += 1;
        }
        catch(Exception ex){
            System.out.println("FAIL: getNotes()");
            failed += 1;
        }

        if(failed != 0){
            throw new Exception(failed + " tests failed!");
        }
    }

    public final String BASE_URL = "https://api.insight.ly";

    private String apikey;
}
