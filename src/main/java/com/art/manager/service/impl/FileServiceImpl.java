package com.art.manager.service.impl;

import com.art.manager.service.FileService;
import com.art.manager.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务类
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(MultipartFile file, String path) throws Exception {
        return FileUtil.upload(file, path);
    }


}
