package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class BasquePig   {
  
  private String className;

  /**
   **/
  public BasquePig className(String className) {
    this.className = className;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("className")
  @NotNull
  public String getClassName() {
    return className;
  }
  public void setClassName(String className) {
    this.className = className;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BasquePig basquePig = (BasquePig) o;
    return Objects.equals(this.className, basquePig.className);
  }

  @Override
  public int hashCode() {
    return Objects.hash(className);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BasquePig {\n");
    
    sb.append("    className: ").append(toIndentedString(className)).append("\n");
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

