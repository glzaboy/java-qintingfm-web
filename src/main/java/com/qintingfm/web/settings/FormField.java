package com.qintingfm.web.settings;

import lombok.Builder;
import lombok.Data;

/**
 * @author guliuzhong
 */
@Builder
@Data
public class FormField {
    String title;
    String fieldName;
    String value;
    String className;
    String tip;
}
