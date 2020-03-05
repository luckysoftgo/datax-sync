package com.application.base.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.application.base.admin.entity.DataxPlugin;
import com.application.base.admin.mapper.DataxPluginMapper;
import com.application.base.admin.service.DataxPluginService;
import org.springframework.stereotype.Service;

/**
 * datax插件信息表服务实现类
 * @author admin
 * @since 2019-05-20
 * @version v1.0
 */
@Service("dataxPluginService")
public class DataxPluginServiceImpl extends ServiceImpl<DataxPluginMapper, DataxPlugin> implements DataxPluginService {

}