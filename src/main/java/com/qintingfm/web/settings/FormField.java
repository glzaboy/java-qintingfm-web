package com.qintingfm.web.settings;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author guliuzhong
 */
@Builder
@Data
public class FormField implements Serializable {
    String title;
    String fieldName;
    String value;
    String className;
    String tip;
}
