package com.learn.srb.core.service;

import com.learn.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    List<Dict> getDictByPid(String id);

    void updateDict(Dict dict);

    List<Dict> getDictsByDictCode(String dictCode);
}
