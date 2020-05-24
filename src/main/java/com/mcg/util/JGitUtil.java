/*
 * @Copyright (c) 2018 缪聪(mcg-helper@qq.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");  
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at  
 *     
 *     http://www.apache.org/licenses/LICENSE-2.0  
 *     
 * Unless required by applicable law or agreed to in writing, software  
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
 * See the License for the specific language governing permissions and  
 * limitations under the License.
 */

package com.mcg.util;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JGitUtil {
	private static Logger logger = LoggerFactory.getLogger(JGitUtil.class);
	
	public static boolean cloneRepository(String remoteUrl, String branch, String projectPath, String user, String pwd) throws InvalidRemoteException, TransportException, GitAPIException {
    	File projectDir = new File(projectPath);

        UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider(user, pwd);
        try (Git git = Git.cloneRepository()
                .setURI(remoteUrl)
                .setBranch(branch)
                .setDirectory(projectDir)
                .setCredentialsProvider(provider)
                .setProgressMonitor(new CloneProgressMonitor())
                .call()) {
        	
        }
        
        return true;
	}

    private static class CloneProgressMonitor implements ProgressMonitor {
        @Override
        public void start(int totalTasks) {
        	logger.debug("执行 " + totalTasks + " 个克隆任务开始");
        }

        @Override
        public void beginTask(String title, int totalWork) {

        }

        @Override
        public void update(int completed) {
     
        }

        @Override
        public void endTask() {
        	logger.debug("执行克隆任务完成");
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }

	public static boolean pull(String projectPath, String branch, String user, String pwd) throws IOException, WrongRepositoryStateException, InvalidConfigurationException, InvalidRemoteException, CanceledException, RefNotFoundException, RefNotAdvertisedException, NoHeadException, TransportException, GitAPIException {

		try (Git git = Git.open(new File(projectPath)) ) {
			UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider(user, pwd);
			git.pull().setRemoteBranchName(branch)
					.setCredentialsProvider(provider)
					.setProgressMonitor(new PullProgressMonitor())
					.call();
		}
		
		return true;
	}
	
    private static class PullProgressMonitor implements ProgressMonitor {
        @Override
        public void start(int totalTasks) {
        	logger.debug("执行 " + totalTasks + " 个拉取任务开始");
        }

        @Override
        public void beginTask(String title, int totalWork) {
        	
        }

        @Override
        public void update(int completed) {

        }

        @Override
        public void endTask() {
        	logger.debug("执行拉取任务完成");
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }
	
}
