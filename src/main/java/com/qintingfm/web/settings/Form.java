package com.qintingfm.web.settings;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author guliuzhong
 */
@Data
@Builder
public class Form {
    List<FormField> formFields;
    String title;
    String settingName;
}
