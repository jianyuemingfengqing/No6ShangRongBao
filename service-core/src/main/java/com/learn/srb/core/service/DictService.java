package com.learn.srb.core.service;

import com.learn.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.deploy.net.HttpResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author jianyueming
 * @since 2022-11-19
 */
public interface DictService extends IService<Dict> {

    void importDicts(MultipartFile file);
}
