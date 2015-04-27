/*
 * The MIT License (MIT) Copyright (c) 2014 karumi Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
  * do so, subject to the following conditions: The above copyright notice and this permission
  * notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE
  * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
  * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
  * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.karumi.rosie.domain.usercase;

import java.lang.reflect.Method;

/**
 * this is the handler for user cases, in you want to invoke an user case you need call to this
 * class with a valid user case. A valid usercase is this one that have an @usercase annotation.
 */
public class UserCaseHandler {
  private final UserCaseFilter userCaseFilter;
  private TaskScheduler taskScheduler;

  public UserCaseHandler(TaskScheduler taskScheduler) {
    this.taskScheduler = taskScheduler;
    userCaseFilter = new UserCaseFilter();
  }

  /**
   * Invoke an user case without arguments. This user case will invoke outside the main thread, and
   * the response come back on the main thread
   *
   * @param userCase the user case to invoke
   */
  public void execute(Object userCase) {
    execute(userCase, (new UserCaseParams.Builder()).build());
  }

  /**
   * Invoke an user case with arguments. If you don't change it on the params this user case will
   * be invoked outside the main thread and the response come back to the ui thread.
   *
   * @param userCase the
   */
  public void execute(Object userCase, UserCaseParams userCaseParams) {

    Method methodsFiltered = userCaseFilter.filter(userCase, userCaseParams);

    if (methodsFiltered != null) {
      UserCaseWrapper userCaseWrapper = new UserCaseWrapper(userCase, userCaseParams);
      taskScheduler.execute(userCaseWrapper);
    }
  }
}
