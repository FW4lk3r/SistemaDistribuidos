package harreader.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.Objects;

/**
 * Information about events occurring during page load.
 * @see <a href="http://www.softwareishard.com/blog/har-12-spec/#pageTimings">specification</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HarPageTiming {

    protected static final Integer DEFAULT_TIME = -1;

    private Integer onContentLoad = DEFAULT_TIME;
    private Integer onLoad = DEFAULT_TIME;
    private String comment;

    /**
     * @return Duration in ms until content is loaded.
     * {@link #DEFAULT_TIME} when no information available.
     */
    public Integer getOnContentLoad() {
        if (onContentLoad == null) {
            return DEFAULT_TIME;
        }
        return onContentLoad;
    }

    public void setOnContentLoad(Integer onContentLoad) {
        this.onContentLoad = onContentLoad;
    }

    /**
     * @return Duration in ms until onLoad event is fired.
     * {@link #DEFAULT_TIME} when no information available.
     */
    public Integer getOnLoad() {
        if (onLoad == null) {
            return DEFAULT_TIME;
        }
        return onLoad;
    }

    public void setOnLoad(Integer onLoad) {
        this.onLoad = onLoad;
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

    public void writeHar(JsonGenerator g) throws JsonGenerationException, IOException {
        g.writeObjectFieldStart("pageTimings");
        if (this.onContentLoad != null) {
            g.writeNumberField("onContentLoad", this.onContentLoad);
        }

        if (this.onLoad != null) {
            g.writeNumberField("onLoad", this.onLoad);
        }

        if (this.comment != null) {
            g.writeStringField("comment", this.comment);
        }

       // this.customFields.writeHar(g);
        g.writeEndObject();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HarPageTiming that = (HarPageTiming) o;
        return Objects.equals(onContentLoad, that.onContentLoad) &&
                Objects.equals(onLoad, that.onLoad) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(onContentLoad, onLoad, comment);
    }
}
