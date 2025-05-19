package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.openapitools.model.EquilateralTriangle;
import org.openapitools.model.IsoscelesTriangle;
import org.openapitools.model.ScaleneTriangle;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "triangleType", visible = true)
@JsonSubTypes({
})

public class Triangle   {
  
  private String shapeType;

  private String triangleType;

  /**
   **/
  public Triangle shapeType(String shapeType) {
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
  public Triangle triangleType(String triangleType) {
    this.triangleType = triangleType;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("triangleType")
  @NotNull
  public String getTriangleType() {
    return triangleType;
  }
  public void setTriangleType(String triangleType) {
    this.triangleType = triangleType;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Triangle triangle = (Triangle) o;
    return Objects.equals(this.shapeType, triangle.shapeType) &&
        Objects.equals(this.triangleType, triangle.triangleType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shapeType, triangleType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Triangle {\n");
    
    sb.append("    shapeType: ").append(toIndentedString(shapeType)).append("\n");
    sb.append("    triangleType: ").append(toIndentedString(triangleType)).append("\n");
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

