package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.model.Quadrilateral;
import org.openapitools.model.Triangle;
import javax.validation.constraints.*;
import javax.validation.Valid;

/**
 * The value may be a shape or the &#39;null&#39; value. The &#39;nullable&#39; attribute was introduced in OAS schema &gt;&#x3D; 3.0 and has been deprecated in OAS schema &gt;&#x3D; 3.1.
 **/

import io.swagger.annotations.*;
import java.util.Objects;


@ApiModel(description = "The value may be a shape or the 'null' value. The 'nullable' attribute was introduced in OAS schema >= 3.0 and has been deprecated in OAS schema >= 3.1.")@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "shapeType", visible = true)
@JsonSubTypes({
})

public class NullableShape   {
  
  private String shapeType;

  private String quadrilateralType;

  /**
   **/
  public NullableShape shapeType(String shapeType) {
    this.shapeType = shapeType;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("shapeType")
  @NotNull
  public String getShapeType() {
    return shapeType;
  }
  public void setShapeType(String shapeType) {
    this.shapeType = shapeType;
  }


  /**
   **/
  public NullableShape quadrilateralType(String quadrilateralType) {
    this.quadrilateralType = quadrilateralType;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("quadrilateralType")
  @NotNull
  public String getQuadrilateralType() {
    return quadrilateralType;
  }
  public void setQuadrilateralType(String quadrilateralType) {
    this.quadrilateralType = quadrilateralType;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NullableShape nullableShape = (NullableShape) o;
    return Objects.equals(this.shapeType, nullableShape.shapeType) &&
        Objects.equals(this.quadrilateralType, nullableShape.quadrilateralType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shapeType, quadrilateralType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NullableShape {\n");
    
    sb.append("    shapeType: ").append(toIndentedString(shapeType)).append("\n");
    sb.append("    quadrilateralType: ").append(toIndentedString(quadrilateralType)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

