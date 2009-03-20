package org.apache.maven.project.processor;

import java.util.ArrayList;

import org.apache.maven.model.Model;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.ReportSet;
import org.apache.maven.model.Reporting;

public class ReportingProcessor extends BaseProcessor
{
    public void process( Object parent, Object child, Object target, boolean isChildMostSpecialized )
    {
        super.process( parent, child, target, isChildMostSpecialized );
        
        Model t = (Model) target, c = (Model) child, p = (Model) parent;
        if(c.getReporting() != null)
        {
            if(t.getReporting() == null)
            {
                t.setReporting( new Reporting() );
            }
            
            copy(c.getReporting(), t.getReporting());
        }
        
        if(p != null && p.getReporting() != null)
        {
            if(t.getReporting() == null)
            {
                t.setReporting( new Reporting() );
            }
            
            copy(p.getReporting(), t.getReporting());
        }        
    }
    
    private static void copy(Reporting source, Reporting target)
    {
        if(target.getOutputDirectory() == null)
        {
            target.setOutputDirectory( source.getOutputDirectory() );
            target.setExcludeDefaults( source.isExcludeDefaults() );
            
            for(ReportPlugin plugin : source.getPlugins())
            {
                target.addPlugin( copyPlugin(plugin ) );
            }
        }
    }
    
    private static ReportPlugin copyPlugin(ReportPlugin plugin)
    {
        ReportPlugin rp = new ReportPlugin();
        rp.setArtifactId( plugin.getArtifactId() );
        rp.setGroupId( plugin.getGroupId() );
        rp.setInherited( plugin.getInherited() );
        rp.setVersion( plugin.getVersion() );
        rp.setConfiguration( plugin.getConfiguration() );
        
        for(ReportSet rs : plugin.getReportSets())
        {
            ReportSet r = new ReportSet();
            r.setId( rs.getId() );
            r.setInherited( rs.getInherited() );
            r.setReports( new ArrayList<String>(rs.getReports()) );
            r.setConfiguration( rs.getConfiguration() );
            rp.addReportSet( r );
        }
        return rp;
    }
}