package com.svnlan.home.service;

import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.utils.CompressFileReader;
import com.svnlan.jwt.domain.LoginUser;


/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/25 14:19
 */
public interface ExplorerFileService {

    Object unzipList(HomeExplorerDTO homeExp, LoginUser loginUser);

    CompressFileReader.FileNode getFileNode(CommonSource source, String compressKey, CompressFileReader.FileNode fileNode );

    Boolean checkZipIsEncrypted(HomeExplorerDTO homeExp);
}
