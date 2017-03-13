package io.gxu.anymall.listener;

import io.gxu.anymall.db.PoolManager;

import java.io.FileReader;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class ContextInitializer implements ServletContextListener {

	private Logger log = Logger.getLogger(ContextInitializer.class);
	
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	public void contextInitialized(ServletContextEvent evt) {
		String dbConf = evt.getServletContext().getInitParameter("db.conf");
		if(dbConf.startsWith("classpath:")){
			dbConf=dbConf.replace("classpath:", "").trim();
		}
		String path = this.getClass().getResource("/").getPath()
				+ dbConf;
		log.info(String.format("加载数据库配置: %s", path));
		Properties dbProps = new Properties();
		try {
			dbProps.load(new FileReader(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		PoolManager.getInstance().createPool("default", dbProps.getProperty("driver"),
				dbProps.getProperty("url"), dbProps.getProperty("user"),
				dbProps.getProperty("password"));
	}
}
