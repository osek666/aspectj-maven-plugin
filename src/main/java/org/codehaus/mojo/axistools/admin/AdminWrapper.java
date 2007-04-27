package org.codehaus.mojo.axistools.admin;

import org.apache.axis.AxisEngine;
import org.apache.axis.MessageContext;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.server.AxisServer;
import org.apache.axis.utils.Admin;
import org.apache.axis.utils.Messages;
import org.apache.axis.utils.XMLUtils;
import org.codehaus.mojo.axistools.axis.AxisPluginException;
import org.w3c.dom.Document;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: mlake
 * Date: Jan 23, 2007
 * Time: 11:09:38 AM
 * MusicTodayLLC
 */
public class AdminWrapper extends Admin {
    public void execute(String[] args)
            throws AxisPluginException {
        AdminWrapper adminWrapper = new AdminWrapper();
        int i = 0;

        try {
            AxisEngine engine;
            if (args[0].equals("client"))
                engine = new AxisClient();
            else
                engine = new AxisServer();
            engine.setShouldSaveConfig(false);
            engine.init();
            MessageContext msgContext = new MessageContext(engine);
            Writer osWriter = new OutputStreamWriter(
                        new FileOutputStream(args[1]), XMLUtils.getEncoding());
                PrintWriter writer = new PrintWriter(new BufferedWriter(osWriter));

            try {

                for (i = 2; i < args.length; i++) {
                    if (log.isDebugEnabled())
                        log.debug(Messages.getMessage("process00", args[i]));

                    Document doc = XMLUtils.newDocument(new FileInputStream(args[i]));
                    Document result = adminWrapper.process(msgContext, doc.getDocumentElement());






                    if (result != null) {
                        System.out.println(XMLUtils.DocumentToString(result));
                    }
                }
                Document document = Admin.listConfig(engine);
                XMLUtils.DocumentToWriter(document, writer);
                writer.println();
                writer.close();

            }
            catch (Exception e) {
                log.error(Messages.getMessage("errorProcess00", args[i]), e);
                writer.close();
                throw e;
            }
        } catch (Exception e) {
            throw new AxisPluginException("Axis Admin had a problem, it returned a failure status: " + e.getMessage());
        }

    }
}
