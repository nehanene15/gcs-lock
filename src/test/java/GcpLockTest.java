/*
 * Copyright (C) 2019 Google Inc.
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

    public static void main(String[] args) {
        testLock("my-test-bucket-nnene", 5);
    }

    public static void testLock(String bucketName, long timeout) {
        /**
         * Tests lock integrity. The method acquires one lock and then initiates another
         * request for a lock without unlocking the first. The second request should time out.
         *
         * @param bucketName name of bucket where lock file will reside
         * @param timeout number of seconds to attempt to lock/unlock resource
         */

        try (GcpLock gcpLock1 = new GcpLock(bucketName, timeout)) {
            TimeUnit.SECONDS.sleep(3); // Do work
            // Following requests for locking should be time out since resource is still locked
            try (GcpLock gcpLock2 = new GcpLock(bucketName, timeout)) {
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
