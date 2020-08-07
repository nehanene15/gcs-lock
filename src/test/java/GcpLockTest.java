/*
 * Copyright 2020 Google LLC
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

import java.util.concurrent.TimeUnit;

public class GcpLockTest {
    private static String LOCK_NAME = "lock";

    public static void main(String[] args) {
        // testLock("swast-scratch", 5, TimeUnit.SECONDS);
        testLock("my-test-bucket-nnene", 5, TimeUnit.SECONDS);
    }

    public static void testLock(String bucketName, long timeout, TimeUnit unit) {
        /**
         * Tests lock integrity. The method acquires one lock and then initiates another
         * request for a lock without unlocking the first. The second request should time out.
         *
         * @param bucketName name of bucket where lock file will reside
         * @param timeout number of seconds to attempt to lock/unlock resource
         * @param unit TimeUnit of timeout
         */

        GcpLockFactory lockFactory = new GcpLockFactory(bucketName);

        try (GcpLock gcpLock1 = lockFactory.createLock(LOCK_NAME, timeout, unit)) {
            TimeUnit.SECONDS.sleep(3); // Do work
            // Following requests for locking should be time out since resource is still locked
            try (GcpLock gcpLock2 = lockFactory.createLock(LOCK_NAME, timeout, TimeUnit.SECONDS)) {
                TimeUnit.SECONDS.sleep(3); // Do work
                System.err.println("Test lock failed.");
            } catch (Exception e) {
                System.err.println(e);
            }
            System.out.println("Test lock PASSED.");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
