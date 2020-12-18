//public void setLogMsg(JoinPoint joinPoint, Object keys, Throwable throwable) {
//        //开始时间
//        String startTime = DateUtils.yyyyMMddHHmmssSss();
//        // 接收到请求，记录请求内容
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        //获取请求头中的User-Agent
//        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
//        // 从切面织入点处通过反射机制获取织入点处的方法
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        // 获取切入点所在的方法
//        Method method = signature.getMethod();
//        // 获取操作
//        LOG opLog = method.getAnnotation(LOG.class);
//        // 获取请求的类名
//        String className = joinPoint.getTarget().getClass().getName();
//        // 请求的参数
//        Map<String, String> rtnMap = ComUtils.converMap(request.getParameterMap());
//        rtnMap.remove("password");
//        rtnMap.remove("oldPassword");
//        rtnMap.remove("repassword");
//        // 将参数所在的数组转换成json
//        String req_param = JSONUtils.toJSONString(rtnMap);
//        log.info("StTime ==> " + startTime);
//        log.info("IP     ==> " + ComUtils.getIpAddr(request));
//        log.info("Type   ==> " + request.getMethod());
//        log.info("Class  ==> " + className);
//        log.info("Method ==> " + request.getRequestURI());
//        log.info("Modul  ==> " + opLog.operModul());
//        log.info("Type   ==> " + opLog.operType());
//        log.info("Desc   ==> " + opLog.operDesc());
//        log.info("Req    ==> " + req_param);
//        String resultStr = "";
//        if (Objects.nonNull(keys) && Objects.isNull(throwable)) {
//        //成功
//        resultStr = JSONObject.toJSONString(keys);
//        log.info("Resp   ==> " + resultStr);
//        }
//        if (Objects.isNull(keys) && Objects.nonNull(throwable)) {
//        //失败
//        log.info("EResp  ==> " + throwable.getClass().getName());
//        log.info("EResp  ==> " + throwable.getMessage());
//        }
//        String endTime = DateUtils.yyyyMMddHHmmssSss();
//        log.info("EnTime ==> " + endTime);
//        String sec = DateUtils.getMinSec(startTime, endTime);
//        log.info("Sec    ==> " + sec);
//        log.info("\r");
//
//        Subject subject = SecurityUtils.getSubject();
//        // 当前用户 认证成功的
//        String username = subject.getPrincipal().toString();
//        //行为记录对象
//        LogPojo logPojo = new LogPojo();
//        //操作员
//        logPojo.setUserName(subject.getPrincipal().toString());
//        //硬件设备
//        logPojo.setDevice(userAgent.getBrowser().toString() == null ? "" : userAgent.getBrowser().toString());
//        //设备系统
//        logPojo.setDeviceSys(userAgent.getOperatingSystem().toString() == null ? "" : userAgent.getOperatingSystem().toString());
//        //设备版本
//        if (Objects.isNull(userAgent.getBrowserVersion())) {
//        logPojo.setDeviceV(userAgent.getBrowser().toString());
//        } else {
//        logPojo.setDeviceV(userAgent.getBrowserVersion().toString());
//        }
//        //操作模块
//        logPojo.setOperModule(opLog.operModul());
//        //操作类型
//        logPojo.setOperType(opLog.operType());
//        //操作描述
//        logPojo.setOperMsg(opLog.operDesc());
//        //请求URI
//        logPojo.setReqUri(request.getRequestURI());
//        //请求I P
//        logPojo.setReqIp(ComUtils.getIpAddr(request));
//        logPojo.setOperMethod(className);//操作方法
//        logPojo.setOperTimes(startTime);//操作时间
//        logPojo.setReqParameter(req_param);//请求参数
//        // 耗时
//        logPojo.setTakeUpTime(sec);
//        // 返回结果
//        logPojo.setRespParameter(resultStr);
//        logPojo.setRespTimes(endTime);
//        logPojo.setLogType(request.getRequestURI().contains("/app/login") ? 1 : 2);
//        logPojo.setLogStatus(1);
//
//        }