/*
 * *** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is part of dcm4che, an implementation of DICOM(TM) in
 * Java(TM), hosted at https://github.com/dcm4che.
 *
 * The Initial Developer of the Original Code is
 * J4Care.
 * Portions created by the Initial Developer are Copyright (C) 2016-2017
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * See @authors listed below
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * *** END LICENSE BLOCK *****
 */

package org.dcm4chee.arc.export.mgt;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.dcm4chee.arc.conf.ExporterDescriptor;
import org.dcm4chee.arc.entity.QueueMessage;
import org.dcm4chee.arc.event.QueueMessageEvent;
import org.dcm4chee.arc.qmgt.*;
import org.dcm4chee.arc.store.StoreContext;

import javax.enterprise.event.Observes;
import java.util.Date;
import java.util.List;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @author Vrinda Nayak <vrinda.nayak@j4care.com>
 * @since Feb 2016
 */
public interface ExportManager {
    void onStore(@Observes StoreContext ctx);

    int scheduleExportTasks(int fetchSize);

    void scheduleExportTask(String studyUID, String seriesUID, String objectUID, ExporterDescriptor exporter,
                            HttpServletRequestInfo httpServletRequestInfo, String batchID)
            throws QueueSizeLimitExceededException;

    void updateExportTask(Long pk);

    ExportTaskQuery listExportTasks(Predicate matchQueueMessage, Predicate matchExportTask,
                                    OrderSpecifier<Date> order, int offset, int limit);

    long countExportTasks(Predicate matchQueueMessage, Predicate matchExportTask);

    boolean deleteExportTask(Long pk, QueueMessageEvent queueEvent);

    boolean cancelExportTask(Long pk, QueueMessageEvent queueEvent) throws IllegalTaskStateException;

    long cancelExportTasks(Predicate matchQueueMessage, Predicate matchExportTask, QueueMessage.Status prev)
            throws IllegalTaskStateException;

    boolean rescheduleExportTask(Long pk, ExporterDescriptor exporter, QueueMessageEvent queueEvent)
            throws IllegalTaskStateException, DifferentDeviceException;

    int deleteTasks(Predicate matchQueueMessage, Predicate matchExportTask);

    List<ExportBatch> listExportBatches(Predicate matchQueueBatch, Predicate matchExportBatch,
                                        OrderSpecifier<Date> order, int offset, int limit);
}
