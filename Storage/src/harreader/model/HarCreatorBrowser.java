package harreader.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.Objects;

/**
 * Information about the application/browser used for creating HAR.
 * @see <a href="http://www.softwareishard.com/blog/har-12-spec/#creator">specification</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HarCreatorBrowser {

    private String name;
    private String version;
    private String comment;

    /**
     * @return Name of the application/browser used for creating HAR, null if not present.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Version of the application/browser used for creating HAR, null if not present.
     */
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HarCreatorBrowser that = (HarCreatorBrowser) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(version, that.version) &&
                Objects.equals(comment, that.comment);
    }

    public void writeHar(JsonGenerator g) throws JsonGenerationException, IOException {
        g.writeObjectFieldStart("creator");
        g.writeStringField("name", this.name);
        g.writeStringField("version", this.version);
        if (this.comment != null) {
            g.writeStringField("comment", this.comment);
        }

        // this.customFields.writeHar(g);
        g.writeEndObject();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version, comment);
    }
}
