package harreader.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Information about a single HTTP request.
 * @see <a href="http://www.softwareishard.com/blog/har-12-spec/#entries">specification</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HarEntry {

    private String pageref;
    private Date startedDateTime;
    private Integer time;
    private String _priority;
    private String _resourceType;
    private HarRequest request;
    private HarResponse response;
    private HarCache cache;
    private HarTiming timings;
    private String serverIPAddress;
    private String connection;
    private String comment;
    private Map<String, Object> additional = new HashMap<>();

    /**
     * @return Reference to parent page, to which the request belongs to, null if not present.
     */
    public String getPageref() {
        return pageref;
    }

    public void setPageref(String pageref) {
        this.pageref = pageref;
    }

    public String get_priority() {
        return _priority;
    }

    public void set_priority(String _priority) {
        this._priority = _priority;
    }

    public String get_resourceType() {
        return _resourceType;
    }

    public void set_resourceType(String _resourceType) {
        this._resourceType = _resourceType;
    }

    /**
     * @return Start time of request, null if not present.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Date getStartedDateTime() {
        return startedDateTime;
    }

    public void setStartedDateTime(Date startedDateTime) {
        this.startedDateTime = startedDateTime;
    }

    /**
     * @return Total request time (in ms), null if not present.
     */
    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    /**
     * @return Detailed request information.
     */
    public HarRequest getRequest() {
        if (request == null) {
            request = new HarRequest();
        }
        return request;
    }

    public void setRequest(HarRequest request) {
        this.request = request;
    }

    /**
     * @return Detailed response information.
     */
    public HarResponse getResponse() {
        if (response == null) {
            response = new HarResponse();
        }
        return response;
    }

    public void setResponse(HarResponse response) {
        this.response = response;
    }

    /**
     * @return Information about cache usage.
     */
    public HarCache getCache() {
        if (cache == null) {
            cache = new HarCache();
        }
        return cache;
    }

    public void setCache(HarCache cache) {
        this.cache = cache;
    }

    /**
     * @return Detailed information about request/response timings.
     */
    public HarTiming getTimings() {
        if (timings == null) {
            timings = new HarTiming();
        }
        return timings;
    }

    public void setTimings(HarTiming timings) {
        this.timings = timings;
    }

    /**
     * @return Server IP address (result of DNS resolution), null if not present.
     */
    public String getServerIPAddress() {
        return serverIPAddress;
    }

    public void setServerIPAddress(String serverIPAddress) {
        this.serverIPAddress = serverIPAddress;
    }

    /**
     * @return Unique ID of TCP/IP connection, null if not present.
     */
    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    /**
     * @return Comment provided by the user or application, null if not present.
     */
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditional() {
        return additional;
    }

    @JsonAnySetter
    public void setAdditionalField(String name, Object value) {
        this.additional.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HarEntry harEntry = (HarEntry) o;
        return Objects.equals(pageref, harEntry.pageref) &&
                Objects.equals(startedDateTime, harEntry.startedDateTime) &&
                Objects.equals(time, harEntry.time) &&
                Objects.equals(_resourceType, harEntry._resourceType) &&
                Objects.equals(_priority, harEntry._priority) &&
                Objects.equals(request, harEntry.request) &&
                Objects.equals(response, harEntry.response) &&
                Objects.equals(cache, harEntry.cache) &&
                Objects.equals(timings, harEntry.timings) &&
                Objects.equals(serverIPAddress, harEntry.serverIPAddress) &&
                Objects.equals(connection, harEntry.connection) &&
                Objects.equals(comment, harEntry.comment) &&
                Objects.equals(additional, harEntry.additional);
    }

    public void writeHar(JsonGenerator g) throws JsonGenerationException, IOException {
        g.writeStartObject();
        if (this.pageref != null) {
            g.writeStringField("pageref", this.pageref);
        }

       // g.writeStringField("startedDateTime", String.valueOf(this.startedDateTime));
        g.writeNumberField("time", this.time);
        g.writeStringField("_resourceType", this._resourceType);
        g.writeStringField("_priority", this._priority);
        this.request.writeHar(g);
        this.response.writeHar(g);
       /* if (this.cache != null) {
            this.cache.writeHar(g);
        }*/

       // this.timings.writeHar(g);
        if (this.serverIPAddress != null) {
            g.writeStringField("serverIPAddress", this.serverIPAddress);
        }

        if (this.connection != null) {
            g.writeStringField("connection", this.connection);
        }

        if (this.comment != null) {
            g.writeStringField("comment", this.comment);
        }

        // this.customFields.writeHar(g);
        g.writeEndObject();
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageref, startedDateTime, time, _priority, _resourceType, request, response, cache, timings, serverIPAddress,
                connection, comment, additional);
    }
}
