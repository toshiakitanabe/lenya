/*
$Id: Publication.java,v 1.17 2003/07/17 11:59:52 egli Exp $
<License>

 ============================================================================
                   The Apache Software License, Version 1.1
 ============================================================================

 Copyright (C) 1999-2003 The Apache Software Foundation. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of  source code must  retain the above copyright  notice,
    this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. The end-user documentation included with the redistribution, if any, must
    include  the following  acknowledgment:  "This product includes  software
    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
    Alternately, this  acknowledgment may  appear in the software itself,  if
    and wherever such third-party acknowledgments normally appear.

 4. The names "Apache Lenya" and  "Apache Software Foundation"  must  not  be
    used to  endorse or promote  products derived from  this software without
    prior written permission. For written permission, please contact
    apache@apache.org.

 5. Products  derived from this software may not  be called "Apache", nor may
    "Apache" appear  in their name,  without prior written permission  of the
    Apache Software Foundation.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Apache Software  Foundation and was  originally created by
 Michael Wechner <michi@apache.org>. For more information on the Apache Soft-
 ware Foundation, please see <http://www.apache.org/>.

 Lenya includes software developed by the Apache Software Foundation, W3C,
 DOM4J Project, BitfluxEditor, Xopus, and WebSHPINX.
</License>
*/
package org.apache.lenya.cms.publication;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.lenya.cms.publishing.PublishingEnvironment;
import org.apache.log4j.Category;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A publication.
 *
 * @author <a href="mailto:andreas.hartmann@wyona.org">Andreas Hartmann</a>
 */
public class Publication {
	private static Category log = Category.getInstance(Publication.class);

    public static final String AUTHORING_AREA = "authoring";
    public static final String LIVE_AREA = "live";
    public static final String INFO_AREA = "info";
    public static final String ADMIN_AREA = "admin";

    public static final String PATH_MAPPER = "path-mapper";
    public static final String LANGUAGES = "languages";
    public static final String LANGUAGE = "language";
    public static final String DEFAULT_LANGUAGE_ATTR = "default";

    public static final String PUBLICATION_PREFIX =
        "lenya" + File.separator + "pubs";
	public static final String CONFIGURATION_PATH = "config";

    private static final String[] areas =
        { AUTHORING_AREA, LIVE_AREA, INFO_AREA, ADMIN_AREA };

    private static final String configFileName = "publication.xconf";

    private String id;
    private PublishingEnvironment environment;
    private File servletContext;
    private DocumentIdToPathMapper mapper = new DefaultDocumentIdToPathMapper();
    private ArrayList languages = new ArrayList();
    private String defaultLanguage = null;

    /** 
     * Creates a new instance of Publication
     * 
     * @param id the publication id
     * @param servletContextPath the servlet context of this publication
     * 
     * @throws PublicationException if there was a problem reading the config file
     */
    protected Publication(String id, String servletContextPath)
        throws PublicationException {
        assert id != null;
        this.id = id;

        assert servletContextPath != null;

        File servletContext = new File(servletContextPath);
        assert servletContext.exists();
        this.servletContext = servletContext;

        // FIXME: remove PublishingEnvironment from publication
        environment = new PublishingEnvironment(servletContextPath, id);

        File configFile =
            new File(
                servletContext,
                PUBLICATION_PREFIX
                    + File.separator
                    + id
                    + File.separator
                    + CONFIGURATION_PATH
                    + File.separator
                    + configFileName);
        DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();

        Configuration config;
        String pathMapperClassName = "";
        
        try {
            config = builder.buildFromFile(configFile);
            pathMapperClassName = config.getChild(PATH_MAPPER).getValue();

			Class pathMapperClass = Class.forName(pathMapperClassName);
			mapper = (DocumentIdToPathMapper) pathMapperClass.newInstance();
			

			Configuration[] languages = config.getChild(LANGUAGES).getChildren();
			for (int i = 0; i < languages.length; i++) {
				Configuration languageConfig = languages[i];
                String language = languageConfig.getValue();
                this.languages.add(language);
                if (languageConfig.getAttribute(DEFAULT_LANGUAGE_ATTR, null) != null) {
                    defaultLanguage = language;
                }
            }
			
        } catch (ConfigurationException e) {
            throw new PublicationException(
                "Problem with config file: " + configFile.getAbsolutePath(),
                e);
        } catch (SAXException e) {
            throw new PublicationException(
                "Could not parse config file: " + configFile.getAbsolutePath(),
                e);
        } catch (IOException e) {
        	// FIXME: this code is commented out for backwards
        	// compatibility. As soon as all publications have a 
        	// publication.xconf the code can be uncommented.
//            throw new PublicationException(
//                "Could not find config file: " + configFile.getAbsolutePath(),
//                e);
			log.warn("Could not find config file: " + configFile.getAbsolutePath(), e);
			defaultLanguage = "en";
			mapper = new DefaultDocumentIdToPathMapper();
        } catch (ClassNotFoundException e) {
			throw new PublicationException(
				"Cannot instantiate documentToPathMapper: " + pathMapperClassName,
				e);
        } catch (InstantiationException e) {
			throw new PublicationException(
				"Cannot instantiate documentToPathMapper: " + pathMapperClassName,
				e);
        } catch (IllegalAccessException e) {
			throw new PublicationException(
				"Cannot instantiate documentToPathMapper: " + pathMapperClassName,
				e);
        }
    }

    /**
     * Returns the publication ID.
     * @return A string value.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the publishing environment of this publication.
     * @return A {@link PublishingEnvironment} object.
     * @deprecated It is planned to decouple the environments from the publication.
     */
    public PublishingEnvironment getEnvironment() {
        return environment;
    }

    /**
     * Returns the servlet context this publication belongs to
     * (usually, the <code>webapps/lenya</code> directory).
     * @return A <code>File</code> object.
     */
    public File getServletContext() {
        return servletContext;
    }

    /**
     * Returns the publication directory.
     * @return A <code>File</code> object.
     */
    public File getDirectory() {
        return new File(
            getServletContext(),
            PUBLICATION_PREFIX + File.separator + getId());
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapper DOCUMENT ME!
     */
    public void setPathMapper(DefaultDocumentIdToPathMapper mapper) {
        assert mapper != null;
        this.mapper = mapper;
    }

    /**
     * Returns the path mapper.
     * 
     * @return a <code>DocumentIdToPathMapper</code>
     */
    public DocumentIdToPathMapper getPathMapper() {
        return mapper;
    }

    /**
     * Returns if a given string is a valid area name.
     * @param area The area string to test.
     * @return A boolean value.
     */
    public static boolean isValidArea(String area) {
        return area != null && Arrays.asList(areas).contains(area);
    }

    /**
     * Get the default language
     * 
     * @return the default language
     */
    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * Set the default language
     * 
     * @param language the default language
     */
    public void setDefaultLanguage(String language) {
        defaultLanguage = language;
    }

    /**
     * Get all available languages for this publication
     * 
     * @return an <code>Arrary</code> of languages
     */
    public String[] getLanguages() {
        return (String[])languages.toArray(new String[languages.size()]);
    }

}
