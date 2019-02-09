# -*- coding: utf-8 -*-
"""
Created on Fri Apr  8 16:36:26 2011

@author: ProfMobius & Searge
@version: v1.2
"""
import fnmatch
import warnings
warnings.simplefilter('ignore')
import sys
import logging
import os, shutil, zipfile, glob, csv, re, subprocess
import ConfigParser
import urllib, urllib2
from filehandling.srgsexport import writesrgsfromcsvs
from pylibs.annotate_gl_constants import annotate_file
from pylibs.whereis import whereis
from hashlib import md5


class Commands(object):
    """Contains the commands and initialisation for a full mcp run"""

    MCPVersion = '4.3'
    _instance  = None    #Small trick to create a singleton
    _single    = False   #Small trick to create a singleton
    _default_config = 'conf/mcp.cfg'

    def __init__(self, conffile=None):
        #HINT: This is for the singleton pattern. If we already did __init__, we skip it
        if     Commands._single:return
        if not Commands._single:Commands._single=True

        if sys.version_info[0] == 3:
            print ('ERROR : Python3 is not supported yet.')
            sys.exit(1)

        self.conffile = conffile

        self.readconf()
        self.checkfolders()

        self.startlogger()

        self.logger.info('== MCP v%s =='%self.MCPVersion)

        if   'linux'  in sys.platform:   self.osname='linux'
        elif 'darwin' in sys.platform:   self.osname='osx'
        elif sys.platform[0:3] == 'win': self.osname='win'
        else :
            self.logger.error('OS not supported : %s'%sys.platform)
            sys.exit(0)

        self.logger.debug('OS : %s'%sys.platform)
        self.checkjava()
        self.readcommands()

    #HINT: This is for the singleton pattern. We either create a new instance or return the current one
    def __new__(cls, *args, **kwargs):
        if not cls._instance:
            cls._instance = super(Commands, cls).__new__(cls, *args, **kwargs)
        return cls._instance

    def readcommands(self):
        self.patcher     = self.config.get('COMMANDS', 'Patcher' ).replace('/',os.sep).replace('\\',os.sep)
        self.jadretro    = self.config.get('COMMANDS', 'JadRetro').replace('/',os.sep).replace('\\',os.sep)
        self.jad         = self.config.get('COMMANDS', 'Jad%s'%self.osname     ).replace('/',os.sep).replace('\\',os.sep)
        self.cmdjad      = self.config.get('COMMANDS', 'CmdJad%s'%self.osname  ).replace('/',os.sep).replace('\\',os.sep)
        self.cmdpatch    = self.config.get('COMMANDS', 'CmdPatch%s'%self.osname).replace('/',os.sep).replace('\\',os.sep)
        self.fernflower  = self.config.get('COMMANDS', 'Fernflower').replace('/',os.sep).replace('\\',os.sep)
        self.exceptor    = self.config.get('COMMANDS', 'Exceptor').replace('/',os.sep).replace('\\',os.sep)

        self.cmdrg         = self.config.get('COMMANDS', 'CmdRG',         raw=1)%self.cmdjava
        self.cmdrgreobf    = self.config.get('COMMANDS', 'CmdRGReobf',    raw=1)%self.cmdjava
        self.cmdjadretro   = self.config.get('COMMANDS', 'CmdJadretro',   raw=1)%self.cmdjava
        self.cmdrecompclt  = self.config.get('COMMANDS', 'CmdRecompClt',  raw=1)%self.cmdjavac
        self.cmdrecompsrv  = self.config.get('COMMANDS', 'CmdRecompSrv',  raw=1)%self.cmdjavac
        self.cmdstartsrv   = self.config.get('COMMANDS', 'CmdStartSrv',   raw=1)%self.cmdjava
        self.cmdstartclt   = self.config.get('COMMANDS', 'CmdStartClt',   raw=1)%self.cmdjava
        self.cmdfernflower = self.config.get('COMMANDS', 'CmdFernflower', raw=1)%self.cmdjava
        self.cmdexceptor   = self.config.get('COMMANDS', 'CmdExceptor',   raw=1)%self.cmdjava

    def startlogger(self):
        self.logger = logging.getLogger('MCPLog')
        self.logger.setLevel(logging.DEBUG)
        # create file handler which logs even debug messages
        fh = logging.FileHandler(filename=self.mcplogfile, mode='w')
        fh.setLevel(logging.DEBUG)
        # create console handler with a higher log level
        ch = logging.StreamHandler()
        ch.setLevel(logging.INFO)
        #File output of everything Warning or above
        eh = logging.FileHandler(filename=self.mcperrlogfile, mode='w')
        eh.setLevel(logging.WARNING)
        # create formatter and add it to the handlers
        formatterconsole = logging.Formatter('%(message)s')
        ch.setFormatter(formatterconsole)
        formatterfile = logging.Formatter('%(asctime)s - %(module)11s.%(funcName)s - %(levelname)s - %(message)s',datefmt='%Y-%m-%d %H:%M')
        fh.setFormatter(formatterfile)
        eh.setFormatter(formatterfile)
        # add the handlers to logger
        self.logger.addHandler(ch)
        self.logger.addHandler(fh)
        self.logger.addHandler(eh)

        #HINT: SECONDARY LOGGER FOR CLIENT & SERVER
        self.loggermc = logging.getLogger('MCRunLog')
        self.loggermc.setLevel(logging.DEBUG)
        chmc = logging.StreamHandler()
        chmc.setLevel(logging.DEBUG)
        formatterconsolemc = logging.Formatter('[%(asctime)s] %(message)s',datefmt='%H:%M')
        chmc.setFormatter(formatterconsolemc)
        # add the handlers to logger
        self.loggermc.addHandler(chmc)

    def readconf(self):
        """Read the configuration file to setup some basic paths"""
        config = ConfigParser.SafeConfigParser()
        config.readfp(open(self._default_config))
        if self.conffile is not None:
            config.read(self.conffile)
        self.config = config

        #HINT: We read the directories for cleanup
        try:
            self.dirtemp  = config.get('DEFAULT','DirTemp')
            self.dirsrc   = config.get('DEFAULT','DirSrc')
            self.dirlogs  = config.get('DEFAULT','DirLogs')
            self.dirbin   = config.get('DEFAULT','DirBin')
            self.dirjars  = config.get('DEFAULT','DirJars')
            self.dirreobf = config.get('DEFAULT','DirReobf')
            self.dirlib   = config.get('DEFAULT','DirLib')
            self.dirffout = config.get('DEFAULT','DirFFOut')
        except ConfigParser.NoOptionError:
            pass

        #HINT: We read the position of the CSV files
        self.csvclasses = config.get('CSV', 'Classes')
        self.csvmethods = config.get('CSV', 'Methods')
        self.csvfields  = config.get('CSV', 'Fields')

        #HINT: We read the names of the SRG output
        self.srgsclient = config.get('SRGS', 'Client')
        self.srgsserver = config.get('SRGS', 'Server')

        #HINT: We read the position of the jar files
        self.dirnatives = config.get('JAR', 'DirNatives')
        self.jarclient  = config.get('JAR', 'Client')
        self.jarserver  = config.get('JAR', 'Server')
        self.md5jarclt  = config.get('JAR', 'MD5Client')
        self.md5jarsrv  = config.get('JAR', 'MD5Server')

        #HINT: We read keys relevant to retroguard
        self.retroguard       = config.get('RETROGUARD', 'Location')
        self.rgconfig         = config.get('RETROGUARD', 'RetroConf')
        self.rgclientconf     = config.get('RETROGUARD', 'ClientConf')
        self.rgserverconf     = config.get('RETROGUARD', 'ServerConf')
        self.rgclientout      = config.get('RETROGUARD', 'ClientOut')
        self.rgserverout      = config.get('RETROGUARD', 'ServerOut')
        self.rgclientlog      = config.get('RETROGUARD', 'ClientLog')
        self.rgserverlog      = config.get('RETROGUARD', 'ServerLog')
        self.rgclientdeoblog  = config.get('RETROGUARD', 'ClientDeobLog')
        self.rgserverdeoblog  = config.get('RETROGUARD', 'ServerDeobLog')

        #HINT: We read keys relevant to exceptor
        self.xclientconf = config.get('EXCEPTOR', 'XClientCfg')
        self.xserverconf = config.get('EXCEPTOR', 'XServerCfg')
        self.xclientout  = config.get('EXCEPTOR', 'XClientOut')
        self.xserverout  = config.get('EXCEPTOR', 'XServerOut')
        self.xclientlog  = config.get('EXCEPTOR', 'XClientLog')
        self.xserverlog  = config.get('EXCEPTOR', 'XServerLog')

        #HINT: We read keys relevant to fernflower
        self.ffclientconf = config.get('DECOMPILE', 'FFClientConf')
        self.ffserverconf = config.get('DECOMPILE', 'FFServerConf')
        self.ffclientout  = config.get('DECOMPILE', 'FFClientOut')
        self.ffserverout  = config.get('DECOMPILE', 'FFServerOut')
        self.ffclientsrc  = config.get('DECOMPILE', 'FFClientSrc')
        self.ffserversrc  = config.get('DECOMPILE', 'FFServerSrc')
        self.ffsource     = config.get('DECOMPILE', 'FFSource')

        #HINT: We read the output directories
        self.binouttmp    = config.get('OUTPUT', 'BinOut')
        self.binclienttmp = config.get('OUTPUT', 'BinClient')
        self.binservertmp = config.get('OUTPUT', 'BinServer')
        self.srcclient    = config.get('OUTPUT', 'SrcClient')
        self.srcserver    = config.get('OUTPUT', 'SrcServer')

        #HINT: The packages on the client & server side
        self.pkgclient = config.get('PACKAGES', 'PkgClient').split(',')
        self.pkgserver = config.get('PACKAGES', 'PkgServer').split(',')

        #HINT: Patcher related configs
        self.patchclient   = config.get('PATCHES', 'PatchClient')
        self.patchserver   = config.get('PATCHES', 'PatchServer')
        self.patchtemp     = config.get('PATCHES', 'PatchTemp')
        self.ffpatchclient = config.get('PATCHES', 'FFPatchClient')
        self.ffpatchserver = config.get('PATCHES', 'FFPatchServer')

        #HINT: Recompilation related configs
        try:
            self.binclient    = config.get('RECOMPILE','BinClient')
            self.binserver    = config.get('RECOMPILE','BinServer')
            self.cpathclient  = config.get('RECOMPILE','ClassPathClient').split(',')
            self.fixesclient  = config.get('RECOMPILE','ClientFixes')
            self.cpathserver  = config.get('RECOMPILE','ClassPathServer').split(',')
        except ConfigParser.NoOptionError:
            pass

        #HINT: Reobf related configs
        self.saffxclient    = config.get('REOBF', 'SAFFXClient')
        self.saffxserver    = config.get('REOBF', 'SAFFXServer')
        self.md5client      = config.get('REOBF', 'MD5Client')
        self.md5server      = config.get('REOBF', 'MD5Server')
        self.md5reobfclient = config.get('REOBF', 'MD5PreReobfClient')
        self.md5reobfserver = config.get('REOBF', 'MD5PreReobfServer')
        self.reobsrgclient  = config.get('REOBF', 'ObfSRGClient')
        self.reobsrgserver  = config.get('REOBF', 'ObfSRGServer')
        self.cmpjarclient   = config.get('REOBF', 'RecompJarClient')
        self.cmpjarserver   = config.get('REOBF', 'RecompJarServer')
        self.reobfjarclient = config.get('REOBF', 'ObfJarClient')
        self.reobfjarserver = config.get('REOBF', 'ObfJarServer')
        self.nullpkg        = config.get('REOBF', 'NullPkg')
        self.ignorepkg      = config.get('REOBF', 'IgnorePkg').split(',')
        self.dirreobfclt    = config.get('REOBF', 'ReobfDirClient')
        self.dirreobfsrv    = config.get('REOBF', 'ReobfDirServer')
        self.clientreoblog  = config.get('REOBF', 'ReobfClientLog')
        self.serverreoblog  = config.get('REOBF', 'ReobfServerLog')
        self.fixsound       = config.get('REOBF', 'FixSound')
        self.fixstart       = config.get('REOBF', 'FixStart')

        self.mcplogfile     = config.get('MCP', 'LogFile')
        self.mcperrlogfile  = config.get('MCP', 'LogFileErr')

        try:
            self.rgreobconfig   = config.get('RETROGUARD', 'RetroReobConf')
            self.rgclientreobconf = config.get('RETROGUARD', 'ClientReobConf')
            self.rgserverreobconf = config.get('RETROGUARD', 'ServerReobConf')
        except ConfigParser.NoOptionError:
            pass

    def creatergcfg(self):
        """Create the files necessary for both deobf and obf RetroGuard"""
        self.createsinglergcfg()
        self.createsinglergcfg(True)

    def createsinglergcfg(self, reobf=False):
        """Create the files necessary for RetroGuard"""
        if reobf:
            rgout = open(self.rgreobconfig, 'wb')
        else:
            rgout = open(self.rgconfig, 'wb')
        rgout.write('.option Application\n')
        rgout.write('.option Applet\n')
        rgout.write('.option Repackage\n')

        rgout.write('.option Annotations\n')
        rgout.write('.option MapClassString\n')
        rgout.write('.attribute LineNumberTable\n')
        rgout.write('.attribute EnclosingMethod\n')
        rgout.write('.attribute Deprecated\n')

        if reobf:
            # this is obfuscated in vanilla and breaks the patches
            rgout.write('.attribute SourceFile\n')

            # this will mess up the patches with mods:
            rgout.write('.attribute LocalVariableTable\n')

            # rg doesn't remap generic signatures:
            rgout.write('.option Generic\n')
            rgout.write('.attribute LocalVariableTypeTable\n')

        rgout.close()

        if reobf:
            rgout = open(self.rgclientreobconf,'w')
        else:
            rgout = open(self.rgclientconf,'w')
        rgout.write('%s = %s\n'%('startindex', '0'))
        rgout.write('%s = %s\n'%('input', self.jarclient))
        rgout.write('%s = %s\n'%('output', self.rgclientout))
        rgout.write('%s = %s\n'%('reobinput', self.cmpjarclient))
        rgout.write('%s = %s\n'%('reoboutput', self.reobfjarclient))
        if reobf:
            rgout.write('%s = %s\n'%('script', self.rgreobconfig))
        else:
            rgout.write('%s = %s\n'%('script', self.rgconfig))
        rgout.write('%s = %s\n'%('log', self.rgclientlog))
        rgout.write('%s = %s\n'%('packages', self.srgsclient))
        rgout.write('%s = %s\n'%('classes', self.srgsclient))
        rgout.write('%s = %s\n'%('fields', self.srgsclient))
        rgout.write('%s = %s\n'%('methods', self.srgsclient))
        rgout.write('%s = %s\n'%('reob', self.reobsrgclient))
        rgout.write('%s = %s\n'%('nplog', self.rgclientdeoblog))
        rgout.write('%s = %s\n'%('rolog', self.clientreoblog))
        for pkg in self.ignorepkg:
            rgout.write('%s = %s\n'%('protectedpackage', pkg))
        rgout.close()

        if reobf:
            rgout = open(self.rgserverreobconf,'w')
        else:
            rgout = open(self.rgserverconf,'w')
        rgout.write('%s = %s\n'%('startindex', '0'))
        rgout.write('%s = %s\n'%('input', self.jarserver))
        rgout.write('%s = %s\n'%('output', self.rgserverout))
        rgout.write('%s = %s\n'%('reobinput', self.cmpjarserver))
        rgout.write('%s = %s\n'%('reoboutput', self.reobfjarserver))
        if reobf:
            rgout.write('%s = %s\n'%('script', self.rgreobconfig))
        else:
            rgout.write('%s = %s\n'%('script', self.rgconfig))
        rgout.write('%s = %s\n'%('log', self.rgserverlog))
        rgout.write('%s = %s\n'%('packages', self.srgsserver))
        rgout.write('%s = %s\n'%('classes', self.srgsserver))
        rgout.write('%s = %s\n'%('fields', self.srgsserver))
        rgout.write('%s = %s\n'%('methods', self.srgsserver))
        rgout.write('%s = %s\n'%('reob', self.reobsrgserver))
        rgout.write('%s = %s\n'%('nplog', self.rgserverdeoblog))
        rgout.write('%s = %s\n'%('rolog', self.serverreoblog))
        for pkg in self.ignorepkg:
            rgout.write('%s = %s\n'%('protectedpackage', pkg))
        rgout.close()

    def createsrgs(self, side):
        """Write the srgs files."""
        sidelk = {0:self.srgsclient, 1:self.srgsserver}
        writesrgsfromcsvs(self.csvclasses, self.csvmethods, self.csvfields, sidelk[side], side)

    def createsaffx(self, side):
        """Creates the reobfuscation tables"""
        saffxlk = {0:self.saffxclient,    1:self.saffxserver}

        ff = open(saffxlk[side], 'w')

        ff.write('[OPTIONS]\n')
        ff.write('strip_package net/minecraft/src\n\n')

        #HINT: We read the data from the CSVs and dump it in another formating to a SAFFX file
        methodsreader = csv.DictReader(open(self.csvmethods, 'r'), delimiter=',',quotechar='"', quoting=csv.QUOTE_ALL)
        fieldsreader  = csv.DictReader(open(self.csvfields,  'r'), delimiter=',',quotechar='"', quoting=csv.QUOTE_ALL)
        classesreader = csv.DictReader(open(self.csvclasses, 'r'), delimiter=',',quotechar='"', quoting=csv.QUOTE_ALL)

        ff.write('[CLASSES]\n')
        for row in classesreader:
            if row['name'] == 'Start': continue
            if int(row['side']) == side:
                ff.write('%s/%s %s\n'%(row['package'], row['name'], row['notch']))

        ff.write('[METHODS]\n')
        for row in methodsreader:
            if row['classname'] == 'Start': continue
            if int(row['side']) == side:
                ff.write('%s/%s/%s %s %s\n'%(row['package'], row['classname'], row['name'], row['notchsig'], row['notch']))

        ff.write('[FIELDS]\n')
        for row in fieldsreader:
            if row['classname'] == 'Start': continue
            if int(row['side']) == side:
                ff.write('%s/%s/%s %s\n'%(row['package'], row['classname'], row['name'], row['notch']))

        ff.close()

    def checkjava(self):
        """Check for java and setup the proper directory if needed"""
        results = []
        if self.osname == 'win':
            if subprocess.call('javac.exe 1>NUL 2>NUL', shell=True) == 2:
                self.cmdjava  = 'java.exe'
                self.cmdjavac = 'javac.exe'
                return
            else:
                import _winreg
                for flag in [_winreg.KEY_WOW64_64KEY, _winreg.KEY_WOW64_32KEY]:
                    try:
                        k = _winreg.OpenKey(_winreg.HKEY_LOCAL_MACHINE, "Software\\JavaSoft\\Java Development Kit", 0, _winreg.KEY_READ | flag)
                        version,_ = _winreg.QueryValueEx(k, "CurrentVersion")
                        k.Close()
                        k = _winreg.OpenKey(_winreg.HKEY_LOCAL_MACHINE, "Software\\JavaSoft\\Java Development Kit\\%s" % version, 0, _winreg.KEY_READ | flag)
                        path,_ = _winreg.QueryValueEx(k, "JavaHome")
                        k.Close()
                        if subprocess.call('"%s" 1>NUL 2>NUL' % os.path.join(path, "bin", "javac.exe"), shell=True) == 2:
                            self.cmdjava = '"%s"' % os.path.join(path, "bin", "java.exe")
                            self.cmdjavac = '"%s"' % os.path.join(path, "bin", "javac.exe")
                            return
                    except OSError:
                        pass

                if 'ProgramW6432' in os.environ:
                    results.extend(whereis('javac.exe', os.environ['ProgramW6432']))
                if 'ProgramFiles' in os.environ:
                    results.extend(whereis('javac.exe', os.environ['ProgramFiles']))
                if 'ProgramFiles(x86)' in os.environ:
                    results.extend(whereis('javac.exe', os.environ['ProgramFiles(x86)']))

        if self.osname  in ['linux','osx']:
            if subprocess.call('javac 1> /dev/null 2> /dev/null', shell=True) == 2:
                self.cmdjava  = 'java'
                self.cmdjavac = 'javac'
                return
            else:
                results.extend(whereis('javac', '/usr/bin'))
                results.extend(whereis('javac', '/usr/local/bin'))
                results.extend(whereis('javac', '/opt'))

        if not results:
            self.logger.error('Java SDK is not installed ! Please install java SDK from ???')
            sys.exit(0)
        else:
            if self.osname == 'win':
                self.cmdjavac = '"%s"'%os.path.join(results[0],'javac.exe')
                self.cmdjava  = '"%s"'%os.path.join(results[0],'java.exe')
            if self.osname  in ['linux','osx']:
                self.cmdjavac = os.path.join(results[0],'javac')
                self.cmdjava  = os.path.join(results[0],'java')

    def checkjars(self, side):
        jarlk     = {0:self.jarclient, 1:self.jarserver}
        md5jarlk  = {0:self.md5jarclt, 1:self.md5jarsrv}

        if not os.path.exists(jarlk[side]):
            self.logger.warning('!! Missing jar file %s. Aborting !!'%jarlk[side])
            return False

        md5jar = md5(open(jarlk[side],'rb').read()).hexdigest()

        if not md5jar == md5jarlk[side]:
            self.logger.warn('!! Modified jar detected. Unpredictable results !!')
            self.logger.debug('md5: ' + md5jar)

        return True

    def checksources(self, side):
        srclk = {0:self.srcclient, 1:self.srcserver}
        if side == 0:
            if not os.path.exists(os.path.join(srclk[side], 'net/minecraft/client/Minecraft.java')):
                self.logger.warning('!! Can not find client sources !!')
                return False
            else:
                return True

        if side == 1:
            if not os.path.exists(os.path.join(srclk[side], 'net/minecraft/server/MinecraftServer.java')):
                self.logger.warning('!! Can not find server sources !!')
                return False
            else:
                return True

    def checkbins(self, side):
        binlk = {0:self.binclient, 1:self.binserver}
        if side == 0:
            if not os.path.exists(os.path.join(binlk[side], 'net/minecraft/client/Minecraft.class')):
                self.logger.warning('!! Can not find client bins !!')
                return False
            else:
                return True

        if side == 1:
            if not os.path.exists(os.path.join(binlk[side], 'net/minecraft/server/MinecraftServer.class')):
                self.logger.warning('!! Can not find server bins !!')
                return False
            else:
                return True

    def checkfolders(self):
        try:
            if not os.path.exists(self.dirtemp):
                os.mkdir(self.dirtemp)
            if not os.path.exists(self.dirsrc):
                os.mkdir(self.dirsrc)
            if not os.path.exists(self.dirlogs):
                os.mkdir(self.dirlogs)
            if not os.path.exists(self.dirbin):
                os.mkdir(self.dirbin)
            if not os.path.exists(self.dirreobf):
                os.mkdir(self.dirreobf)
            if not os.path.exists(self.dirlib):
                os.mkdir(self.dirlib)
        except AttributeError:
            pass

    def checkupdates(self, silent=False):
        results = []
        #HINT: Each local entry is of the form dict[filename]=(md5,modificationtime)
        md5lcldict = {}
        for path, dirlist, filelist in os.walk('.'):
            for trgfile in filelist:
                md5lcldict[os.path.join(path,trgfile).replace(os.sep, '/').replace('./','')] = \
                (md5(open(os.path.join(path,trgfile),'rb').read()).hexdigest(),
                 os.stat(os.path.join(path,trgfile)).st_mtime
                 )

        try:
            md5srvlist = urllib.urlopen('http://mcp.ocean-labs.de/files/mcprolling/mcp.md5').readlines()
            md5srvdict = {}
        except IOError:
            return []

        #HINT: Each remote entry is of the form dict[filename]=(md5,modificationtime)
        for entry in md5srvlist:
            md5srvdict[entry.split()[0]] = (entry.split()[1], float(entry.split()[2]), entry.split()[3])

        for key,value in md5srvdict.items():
            #HINT: If the remote entry is not in the local table, append
            if not key in md5lcldict:
                results.append([key, value[0], value[1], value[2]])
                continue
            #HINT: If the remote entry has a different MD5 checksum and modtime is > local entry modtime
            if not md5lcldict[key][0] == value[0] and value[1] > md5lcldict[key][1]:
                results.append([key, value[0], value[1], value[2]])

        if results and not silent:
            self.logger.warning('!! Updates available. Please run updatemcp to get them. !!')

        return results

    def cleanbindirs(self,side):
        pathbinlk    = {0:self.binclient,    1:self.binserver}

        for path, dirlist, filelist in os.walk(pathbinlk[side]):
            for bin_file in glob.glob(os.path.join(path, '*.class')):
                os.remove(bin_file)

    def cleanreobfdir(self, side):
        outpathlk = {0:self.dirreobfclt,    1:self.dirreobfsrv}
        pathbinlk = {0:self.binclient,    1:self.binserver}
        if os.path.exists(outpathlk[side]):
            shutil.rmtree(outpathlk[side], ignore_errors=True)

        shutil.copytree(pathbinlk[side], outpathlk[side])
        for path, dirlist, filelist in os.walk(outpathlk[side]):
            for bin_file in glob.glob(os.path.join(path, '*.class')):
                os.remove(bin_file)

        for i in range(4):
            for path, dirlist, filelist in os.walk(outpathlk[side]):
                if not dirlist and not filelist:
                    shutil.rmtree(path)

        if not os.path.exists(outpathlk[side]):
            os.mkdir(outpathlk[side])

    def cleantempbin(self, side):
        pathbinlk = {0:self.binclienttmp,    1:self.binservertmp}

        if side == 0:
            shutil.rmtree(os.path.join(pathbinlk[side], 'META-INF'), ignore_errors=True)
            shutil.rmtree(os.path.join(pathbinlk[side], 'net'), ignore_errors=True)
            shutil.rmtree(os.path.join(pathbinlk[side], 'com'), ignore_errors=True)
            shutil.rmtree(os.path.join(pathbinlk[side], 'paulscode'), ignore_errors=True)

        if side == 1:
            shutil.rmtree(os.path.join(pathbinlk[side], 'META-INF'), ignore_errors=True)
            shutil.rmtree(os.path.join(pathbinlk[side], 'net'), ignore_errors=True)

    def applyrg(self, side):
        """Apply rg to the given side"""

        # add retroguard.jar to copy of client classpath
        if side == 0:
            rgconf = self.rgclientconf
            rgcp = [self.retroguard] + self.cpathclient
            rgcp = os.pathsep.join(rgcp)

        # add retroguard.jar to copy of server classpath
        if side == 1:
            rgconf = self.rgserverconf
            rgcp = [self.retroguard] + self.cpathserver
            rgcp = os.pathsep.join(rgcp)

        forkcmd = self.cmdrg.format(classpath=rgcp, conffile=rgconf)
        self.runcmd(forkcmd)

    def applyff(self, side):
        """Apply fernflower to the given side"""

        if side == 0:
            ffconf = self.ffclientconf
            ffsrc  = self.xclientout

        if side == 1:
            ffconf = self.ffserverconf
            ffsrc  = self.xserverout

        forkcmd = self.cmdfernflower.format(jarff=self.fernflower, conf=ffconf, jarin=ffsrc, jarout=self.dirffout)
        self.runcmd(forkcmd)

    def applyexceptor(self, side):
        """Apply exceptor to the given side"""
        excinput = {0:self.rgclientout,    1:self.rgserverout}
        excoutput = {0:self.xclientout,    1:self.xserverout}
        excconf = {0:self.xclientconf,    1:self.xserverconf}
        exclog = {0:self.xclientlog,    1:self.xserverlog}

        forkcmd = self.cmdexceptor.format(jarexc=self.exceptor, input=excinput[side], output=excoutput[side], conf=excconf[side], log=exclog[side])
        self.runcmd(forkcmd)

    def applyjadretro(self, side):
        """Apply jadretro to the bin output directory"""
        pathbinlk = {0:self.binclienttmp,   1:self.binservertmp}
        pkglist = []
        for path, dirlist, filelist in os.walk(pathbinlk[side]):
            if glob.glob(os.path.join(path,'*.class')):
                for pkg in self.ignorepkg:
                    if pkg.replace('\\',os.sep).replace('/',os.sep) in path:
                        break
                else:
                    pkglist.append(path)

        for pkg in pkglist:
            forkcmd = self.cmdjadretro.format(jarjr=self.jadretro, targetdir=pkg)
            self.runcmd(forkcmd)

    def applyjad(self, side):
        """Decompile the code using jad"""
        pathbinlk = {0:self.binclienttmp, 1:self.binservertmp}
        pathsrclk = {0:self.srcclient,    1:self.srcserver}

        #HINT: We delete the old sources and recreate it
        if os.path.exists(pathsrclk[side]):
            shutil.rmtree(pathsrclk[side])
        os.mkdir(pathsrclk[side])

        #HINT: We go throught the packages and apply jad to the directory
        pkglist = []
        for path, dirlist, filelist in os.walk(pathbinlk[side]):
            if glob.glob(os.path.join(path,'*.class')):
                for pkg in self.ignorepkg:
                    if pkg.replace('\\',os.sep).replace('/',os.sep) in path:
                        break
                else:
                    pkglist.append(path)

        for pkg in pkglist:
            classlist = os.path.join(pkg, '*.class')

            forkcmd = self.cmdjad.format(binjad=self.jad, outdir=pathsrclk[side], classes=classlist)
            self.runcmd(forkcmd)

    def applypatches(self, side):
        """Applies the patches to the src directory"""
        pathsrclk = {0:self.srcclient,   1:self.srcserver}
        patchlk   = {0:self.patchclient, 1:self.patchserver}

        #HINT: Here we transform the patches to match the directory separator of the specific platform
        patch    = open(patchlk[side],'r').read().splitlines()
        outpatch = open(self.patchtemp,'wb')
        for line in patch:
            if line[:3] in ['+++','---', 'Onl', 'dif']:
                 outpatch.write(line.replace('\\',os.sep).replace('/',os.sep) + '\r\n')
            else:
                outpatch.write(line  + '\r\n')
        outpatch.close()

        forkcmd = self.cmdpatch.format(srcdir=pathsrclk[side], patchfile=self.patchtemp)

        p = subprocess.Popen(forkcmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        buffer = []
        errormsgs = []
        retcode = None
        while True:
            o = p.stdout.readline()
            retcode = p.poll()
            if o == '' and retcode is not None:
                break
            if o != '':
                buffer.append(o.strip())

        if retcode == 0:
            for line in buffer:
                self.logger.debug(line)
        else:
            self.logger.warn('%s failed.'%forkcmd)
            self.logger.warn('Return code : %d'%retcode)
            for line in buffer:
                if 'saving rejects' in line:
                    errormsgs.append(line)
                self.logger.debug(line)

            self.logger.warn('')
            self.logger.warn('== ERRORS FOUND ==')
            self.logger.warn('')
            for line in errormsgs:
                self.logger.warn(line)
            self.logger.warn('==================')
            self.logger.warn('')

    def applyffpatches(self, side):
        """Applies the patches to the src directory"""
        pathsrclk = {0:self.srcclient,   1:self.srcserver}
        patchlk   = {0:self.ffpatchclient, 1:self.ffpatchserver}

        #HINT: Here we transform the patches to match the directory separator of the specific platform
        patch    = open(patchlk[side],'r').read().splitlines()
        outpatch = open(self.patchtemp,'wb')
        for line in patch:
            if line[:3] in ['+++','---', 'Onl', 'dif']:
                 outpatch.write(line.replace('\\',os.sep).replace('/',os.sep) + '\r\n')
            else:
                outpatch.write(line  + '\r\n')
        outpatch.close()

        forkcmd = self.cmdpatch.format(srcdir=pathsrclk[side], patchfile=self.patchtemp)

        p = subprocess.Popen(forkcmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        buffer = []
        errormsgs = []
        retcode = None
        while True:
            o = p.stdout.readline()
            retcode = p.poll()
            if o == '' and retcode is not None:
                break
            if o != '':
                buffer.append(o.strip())

        if retcode == 0:
            for line in buffer:
                self.logger.debug(line)
        else:
            self.logger.warn('%s failed.'%forkcmd)
            self.logger.warn('Return code : %d'%retcode)
            for line in buffer:
                if 'saving rejects' in line:
                    errormsgs.append(line)
                self.logger.debug(line)

            self.logger.warn('')
            self.logger.warn('== ERRORS FOUND ==')
            self.logger.warn('')
            for line in errormsgs:
                self.logger.warn(line)
            self.logger.warn('==================')
            self.logger.warn('')

    def recompile(self, side):
        """Recompile the sources and produce the final bins"""
        cmdlk     = {0:self.cmdrecompclt, 1:self.cmdrecompsrv}
        pathbinlk = {0:self.binclient,    1:self.binserver}
        pathsrclk = {0:self.srcclient,    1:self.srcserver}

        if not os.path.exists(pathbinlk[side]):
            os.mkdir(pathbinlk[side])

        #HINT: We create the list of source directories based on the list of packages
        pkglist = ''
        for path, dirlist, filelist in os.walk(pathsrclk[side]):
            if glob.glob(os.path.join(path,'*.java')):
                pkglist += os.path.join(path,'*.java') + ' '

        #HINT: We have to split between client & server because both have different arguments
        forkcmd = ''
        if side == 0:
            cpc = os.pathsep.join(self.cpathclient)
            forkcmd = cmdlk[side].format(classpath=cpc, sourcepath=pathsrclk[side], outpath=pathbinlk[side], pkgs=pkglist, fixes=self.fixesclient)

        if side == 1:
            cps = os.pathsep.join(self.cpathserver)
            forkcmd = cmdlk[side].format(classpath=cps, sourcepath=pathsrclk[side], outpath=pathbinlk[side], pkgs=pkglist)

        self.logger.debug("recompile: '"+forkcmd+"'")
        p = subprocess.Popen(forkcmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        buffer = []
        errormsgs = []
        retcode = None
        while True:
            o = p.stdout.readline()
            retcode = p.poll()
            if o == '' and retcode is not None:
                break
            if o != '':
                buffer.append(o.strip())

        if retcode == 0:
            for line in buffer:
                self.logger.debug(line)
        else:
            self.logger.error('%s failed.'%forkcmd)
            self.logger.error('Return code : %d'%retcode)
            for line in buffer:
                if not line.strip(): continue
                if line[0] != '[' and line[0:4] != 'Note':
                    errormsgs.append(line)
                self.logger.debug(line)

            self.logger.error('')
            self.logger.error('== ERRORS FOUND ==')
            self.logger.error('')
            for line in errormsgs:
                self.logger.error(line)
                if '^' in line: self.logger.error('')
            self.logger.error('==================')
            self.logger.error('')
            #sys.exit(1)

    def startserver(self):
        cps = ['../'+p for p in self.cpathserver]
        cps.insert(2, '../'+self.binserver)
        cps = os.pathsep.join(cps)
        #self.logger.info("classpath: '"+cps+"'")

        os.chdir(self.dirjars)

        forkcmd = self.cmdstartsrv.format(classpath=cps)
        self.runmc(forkcmd)

    def startclient(self):
        cpc = ['../'+p for p in self.cpathclient]
        cpc.insert(2, '../'+self.binclient)
        cpc = os.pathsep.join(cpc)
        #self.logger.info("classpath: '"+cpc+"'")

        os.chdir(self.dirjars)

        forkcmd = self.cmdstartclt.format(classpath=cpc, natives='../'+self.dirnatives)
        self.runmc(forkcmd)

    def runcmd(self, forkcmd):
        self.logger.debug("runcmd: '"+forkcmd+"'")
        p = subprocess.Popen(forkcmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        buffer = []
        retcode = None
        while True:
            o = p.stdout.readline()
            retcode = p.poll()
            if o == '' and retcode is not None:
                break
            if o != '':
                buffer.append(o.strip())

        if retcode == 0:
            for line in buffer:
                self.logger.debug(line)
        else:
            self.logger.error('%s failed.'%forkcmd)
            self.logger.error('Return code : %d'%retcode)
            for line in buffer:
                self.logger.error(line)

    def runmc(self, forkcmd):
        self.logger.debug("runmc: '"+forkcmd+"'")
        pclient = subprocess.Popen(forkcmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        msgs        = []
        returnvalue = None
        while True:
            o = pclient.stdout.readline()
            returnvalue = pclient.poll()
            if o == '' and returnvalue is not None:
                break
            if o != '':
                self.loggermc.debug(o.strip())
                msgs.append(o.strip())

        if returnvalue != 0:
            for msg in msgs:
                self.logger.error(msg)
        else:
            for msg in msgs:
                self.logger.debug(msg)

    def extractjar(self, side):
        """Unzip the jar file to the bin directory defined in the config file"""
        pathbinlk = {0:self.binclienttmp,   1:self.binservertmp}
        jarlk     = {0:self.xclientout, 1:self.xserverout}

        #HINT: We check if the top output directory exists. If not, we create it
        #We than check if the specific side directory exists. If it does, we delete it and create a new one
        if not os.path.exists(self.binouttmp):
            os.mkdir(self.binouttmp)
        if os.path.exists(pathbinlk[side]):
            shutil.rmtree(pathbinlk[side])
        os.mkdir(pathbinlk[side])

        #HINT: We extract the jar to the right location
        zipjar = zipfile.ZipFile(jarlk[side])
        zipjar.extractall(pathbinlk[side])

    def extractsrc(self, side):
        """Unzip the source jar file to the src directory defined in the config file"""
        pathbinlk = {0:self.ffclientout,   1:self.ffserverout}
        jarlk     = {0:self.ffclientsrc, 1:self.ffserversrc}
        pathsrclk = {0:self.srcclient,   1:self.srcserver}

        #HINT: We check if the top output directory exists. If not, we create it
        if not os.path.exists(pathbinlk[side]):
            os.mkdir(pathbinlk[side])

        #HINT: We extract the jar to the right location
        zipjar = zipfile.ZipFile(jarlk[side])
        zipjar.extractall(pathbinlk[side])

        self.copyandfixsrc(pathbinlk[side], pathsrclk[side])

    def copyandfixsrc(self, src_dir, dest_dir):
        src_dir = os.path.normpath(src_dir)
        dest_dir = os.path.normpath(dest_dir)

        for path, dirlist, filelist in os.walk(src_dir):
            sub_dir = os.path.relpath(path, src_dir)
            if sub_dir == '.':
                sub_dir = ''

            for cur_dir in dirlist:
                if os.path.join(sub_dir, cur_dir).replace(os.sep, '/') in self.ignorepkg:
                    # if the full subdir is in the ignored package list delete it so that we don't descend into it
                    dirlist.remove(cur_dir)

            for cur_file in fnmatch.filter(filelist, '*.java'):
                src_file = os.path.join(src_dir, sub_dir, cur_file)
                dest_file = os.path.join(dest_dir, sub_dir, cur_file)

                if not os.path.exists(os.path.dirname(dest_file)):
                    os.makedirs(os.path.dirname(dest_file))

                # don't bother fixing line endings in windows
                if self.osname == 'win':
                    shutil.copyfile(src_file, dest_file)
                else:
                    # read each line in the file, stripping existing line end and adding dos line end
                    with open(src_file, 'r') as in_file:
                        with open(dest_file, 'wb') as out_file:
                            for line in in_file:
                                out_file.write(line.rstrip() + '\r\n')

    def rename(self, side):
        """Rename the sources using the CSV data"""
        pathsrclk = {0:self.srcclient,    1:self.srcserver}

        #HINT: We read the relevant CSVs
        methodsreader = csv.DictReader(open(self.csvmethods, 'r'), delimiter=',',quotechar='"', quoting=csv.QUOTE_ALL)
        fieldsreader  = csv.DictReader(open(self.csvfields,  'r'), delimiter=',',quotechar='"', quoting=csv.QUOTE_ALL)

        methods = {}
        fields  = {}
        for row in methodsreader:
            if int(row['side']) == side:
                if row['searge'] in methods: self.logger.debug('WTF ? %s'%row['searge'])
                methods[row['searge']] = row
        for row in fieldsreader:
            if int(row['side']) == side:
                if row['searge'] in methods: self.logger.debug('WTF ? %s'%row['searge'])
                fields[row['searge']]  = row

        type_hash         = {'methods':'func', 'fields':'field'}
        regexp_searge     = r'%s_[0-9]+_[a-zA-Z]+_?'

        #HINT: We pathwalk the sources
        for path, dirlist, filelist in os.walk(pathsrclk[side]):
            for src_file in glob.glob(os.path.join(path, '*.java')):

                ff    = open(src_file, 'r')
                fftmp = open(src_file + '.tmp', 'w')

                buffer = ff.read()
                ff.close()

                #HINT: We check if the sources have func_????_? or field_????_? in them.
                # If yes, we replace with the relevant information
                for group in ['methods', 'fields']:
                    results = re.findall(regexp_searge%type_hash[group], buffer)

                    for result in results:
                        #HINT: It is possible for the csv to contain data from previous version or enums, so we catch those
                        try:
                            buffer = buffer.replace(result, locals()[group][result]['name'])
                        except KeyError as msg:
                            self.logger.debug("Can not replace %s on side %d"%(msg,side))

                fftmp.write(buffer)
                fftmp.close()

                shutil.move(src_file + '.tmp', src_file)

                #HINT: We annotate the GL constants
                annotate_file(src_file)

    def renamereobsrg(self, side):
        deoblk = {0:self.rgclientdeoblog, 1:self.rgserverdeoblog}
        reoblk = {0:self.reobsrgclient, 1:self.reobsrgserver}

        deoblog = open(deoblk[side],'r').read()
        reobsrg = open(reoblk[side],'w')

        methodsreader = csv.DictReader(open(self.csvmethods, 'r'), delimiter=',',quotechar='"', quoting=csv.QUOTE_ALL)
        fieldsreader  = csv.DictReader(open(self.csvfields,  'r'), delimiter=',',quotechar='"', quoting=csv.QUOTE_ALL)

        #TODO: A bit too much brute force and a bit slow.
        for row in methodsreader:
            if int(row['side']) == side:
                deoblog = deoblog.replace(row['searge'], row['name'])
        for row in fieldsreader:
            if int(row['side']) == side:
                deoblog = deoblog.replace(row['searge'], row['name'])

        reobsrg.write(deoblog)
        reobsrg.close()

    def gathermd5s(self, side, reobf=False):
        if not reobf:
            md5lk     = {0:self.md5client,    1:self.md5server}
        else:
            md5lk     = {0:self.md5reobfclient,    1:self.md5reobfserver}
        pathbinlk = {0:self.binclient,    1:self.binserver}

        md5file = open(md5lk[side],'w')

        #HINT: We pathwalk the recompiled classes
        for path, dirlist, filelist in os.walk(pathbinlk[side]):
            for bin_file in glob.glob(os.path.join(path, '*.class')):
                bin_file_osindep = os.sep.join(bin_file.replace(os.sep,'/').split('/')[2:]).split('.')[0]
                md5file.write('%s %s\n'%(bin_file_osindep, md5(open(bin_file,'rb').read()).hexdigest()))
        md5file.close()

    def packbin(self, side):
        jarlk     = {0:self.cmpjarclient, 1:self.cmpjarserver}
        pathbinlk = {0:self.binclient,    1:self.binserver}
        pathtmpbinlk = {0:self.binclienttmp,    1:self.binservertmp}

        #HINT: We create the zipfile and add all the files from the bin directory
        zipjar = zipfile.ZipFile(jarlk[side], 'w')
        for path, dirlist, filelist in os.walk(pathbinlk[side]):
            path = path.replace('/',os.sep)
            for bin_file in glob.glob(os.path.join(path, '*.class')):
                if self.fixsound.replace('/',os.sep).replace('\\',os.sep) in bin_file: continue
                if self.fixstart.replace('/',os.sep).replace('\\',os.sep) in bin_file: continue
                zipjar.write(bin_file, os.sep.join(bin_file.split(os.sep)[2:]))

        for pkg in self.ignorepkg:
            curpath = os.path.join(pathtmpbinlk[0],pkg)
            for path, dirlist, filelist in os.walk(curpath):
                path = path.replace('/',os.sep)
                for bin_file in glob.glob(os.path.join(path, '*.class')):
                    zipjar.write(bin_file, os.sep.join(bin_file.split(os.sep)[3:]))

        zipjar.close()

    def reobfuscate(self, side):
        # add retroguard.jar to copy of client classpath
        if side == 0:
            rgconf = self.rgclientreobconf
            rgcp = [self.retroguard] + self.cpathclient
            rgcp = os.pathsep.join(rgcp)

        # add retroguard.jar to copy of server classpath
        if side == 1:
            rgconf = self.rgserverreobconf
            rgcp = [self.retroguard] + self.cpathserver
            rgcp = os.pathsep.join(rgcp)

        forkcmd = self.cmdrgreobf.format(classpath=rgcp, conffile=rgconf)
        self.runcmd(forkcmd)

    def unpackreobfclasses(self, side):
        jarlk     = {0:self.reobfjarclient, 1:self.reobfjarserver}
        md5lk     = {0:self.md5client,      1:self.md5server}
        md5reoblk = {0:self.md5reobfclient, 1:self.md5reobfserver}
        outpathlk = {0:self.dirreobfclt,    1:self.dirreobfsrv}

        #HINT: We need a table for the old md5 and the new ones
        md5table     = {}
        md5reobtable = {}
        for row in open(md5lk[side],'r').read().splitlines():
            row = row.strip().split()
            if len(row) == 2:
                md5table[row[0].replace(os.sep,'/')] = row[1]
        for row in open(md5reoblk[side],'r').read().splitlines():
            row = row.strip().split()
            if len(row) == 2:
                md5reobtable[row[0].replace(os.sep,'/')] = row[1]

        trgclasses = []
        for key,value in md5reobtable.items():
            if not key in md5table:
                self.logger.info ('> New class found      : %s'%key)
                trgclasses.append(key.split('.')[0])
                continue
            if not md5table[key] == md5reobtable[key]:
                trgclasses.append(key.split('.')[0])
                self.logger.info ('> Modified class found : %s'%key)

        classesreader = csv.DictReader(open(self.csvclasses, 'r'), delimiter=',',quotechar='"', quoting=csv.QUOTE_ALL)
        classes = {}
        for row in classesreader:
            if int(row['side']) == side:
                #HINT: This pkg equivalence is used to reduce the src pkg to the null one
                pkg = row['package'] + '/'
                if row['package'] == self.nullpkg: pkg = ''
                classes['%s/%s'%(row['package'],row['name'])] = pkg + row['notch']

        if not os.path.exists(outpathlk[side]):
            os.mkdir(outpathlk[side])

        #HINT: We extract the modified class files
        zipjar = zipfile.ZipFile(jarlk[side], 'r')
        for i in trgclasses:
            if i in classes:
                zipjar.extract('%s.class'%classes[i], outpathlk[side])
                self.logger.info ('> Outputted %s to %s as %s'%(i.ljust(35),outpathlk[side],classes[i]+'.class'))
            else:
                i = i.replace(self.nullpkg, '')
                if i[0] == '/': i = i[1:]
                zipjar.extract('%s.class'%i, outpathlk[side])
                self.logger.info ('> Outputted %s to %s as %s'%(i.ljust(35),outpathlk[side],i+'.class'))
        zipjar.close()

    def downloadupdates(self, force=False):
        newfiles = self.checkupdates(silent=True)

        if not newfiles:
            self.logger.info('No new updates found.')
            return

        for entry in newfiles:
            if entry[3] == 'U':
                self.logger.info('New version found for : %s'%entry[0])
            if entry[3] == 'D':
                self.logger.info('File tagged for deletion : %s'%entry[0])

        if 'CHANGELOG' in [i[0] for i in newfiles]:
            print('')
            self.logger.info('== CHANGELOG ==')
            changelog = urllib.urlopen('http://mcp.ocean-labs.de/files/mcprolling/mcp/CHANGELOG').readlines()
            for line in changelog:
                self.logger.info(line.strip())
                if not line.strip():
                    break
            print('')
            print('')

        if not force:
            print('WARNING:')
            print('You are going to update MCP')
            print('Are you sure you want to continue ?')
            answer = raw_input('If you really want to update, enter "Yes" ')
            if not answer.lower() in ['yes','y']:
                print('You have not entered "Yes", aborting the update process')
                sys.exit(0)

        for entry in newfiles:
            if entry[3] == 'U':
                self.logger.info('Retrieving file from server : %s'%entry[0])
                dir = os.path.dirname(entry[0])
                if not os.path.isdir(dir):
                    try:
                        os.makedirs(dir)
                    except OSError:
                        pass

                urllib.urlretrieve('http://mcp.ocean-labs.de/files/mcprolling/mcp/'+entry[0], entry[0])
            if entry[3] == 'D':
                self.logger.info('Removing file from local install : %s'%entry[0])
                #Remove file here
