package com.svnlan.user.controller;

import com.svnlan.user.tools.OptionTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CubeController {
    @Autowired
    OptionTool optionTool;
}
