package hu.tilos.radio.backend;

import org.apache.deltaspike.core.api.jmx.JmxManaged;
import org.apache.deltaspike.core.api.jmx.MBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@MBean(description = "StreamMonitor", name = "StreamerMonitor", category = "hu.tilos.radio.stream")
@Singleton
public class StreamerMonitor implements StreamerMonitorMBean {

    private static final Logger LOG = LoggerFactory.getLogger(StreamerMonitor.class);

    @JmxManaged(description = "description currently active archive listeners")
    private volatile int counter;

    public int getCounter() {
        return counter;
    }

    public void decrement() {
        this.counter--;
    }

    public void increment() {

        this.counter++;
    }
}
