# -*- coding: utf-8 -*-
"""
Created on Fri Apr  8 16:54:36 2011

@author: ProfMobius
@version: v1.2
"""
import sys, time, os
from optparse import OptionParser
from commands import Commands
import recompile

def main(conffile=None, force_jad=False):
    commands = Commands(conffile)

    cltdone = False
    srvdone = False

    use_ff = os.path.exists(commands.fernflower) and not force_jad

    commands.logger.info ('> Creating Retroguard config files')
    commands.creatergcfg()

    srcdir = os.path.join(commands.srcclient, commands.ffsource).replace('/',os.sep).replace('\\',os.sep)
    if not os.path.exists(srcdir):
        commands.logger.info ('== Decompiling Client ==')
        if commands.checkjars(0):
            clienttime = time.time()
            commands.logger.info ('> Creating SRGS for client')
            commands.createsrgs(0)
            commands.logger.info ('> Applying Retroguard to client')
            commands.applyrg(0)
            commands.logger.info ('> Applying Exceptor to client')
            commands.applyexceptor(0)
            if use_ff:
                commands.logger.info ('> Decompiling...')
                commands.applyff(0)
                commands.logger.info ('> Unzipping the client sources')
                commands.extractsrc(0)
            commands.logger.info ('> Unzipping the client jar')
            commands.extractjar(0)
            if not use_ff:
                commands.logger.info ('> Applying jadretro')
                commands.applyjadretro(0)
                commands.logger.info ('> Decompiling...')
                commands.applyjad(0)
            commands.logger.info ('> Applying patches')
            if not use_ff:
                commands.applypatches(0)
            else:
                commands.applyffpatches(0)
            commands.logger.info ('> Renaming sources')
            commands.rename(0)
            commands.logger.info ('> Creating reobfuscation tables')
            commands.renamereobsrg(0)
            commands.logger.info ('> Done in %.2f seconds'%(time.time()-clienttime))
    else:
        commands.logger.warn ('!! Client already decompiled. Run cleanup before decompiling again !!')
        cltdone = True

    srcdir = os.path.join(commands.srcserver, commands.ffsource).replace('/',os.sep).replace('\\',os.sep)
    if not os.path.exists(srcdir):
        commands.logger.info ('== Decompiling Server ==')
        if commands.checkjars(1):
            servertime = time.time()
            commands.logger.info ('> Creating SRGS for server')
            commands.createsrgs(1)
            commands.logger.info ('> Applying Retroguard to server')
            commands.applyrg(1)
            commands.logger.info ('> Applying Exceptor to server')
            commands.applyexceptor(1)
            if use_ff:
                commands.logger.info ('> Decompiling...')
                commands.applyff(1)
                commands.logger.info ('> Unzipping the server sources')
                commands.extractsrc(1)
            commands.logger.info ('> Unzipping the server jar')
            commands.extractjar(1)
            if not use_ff:
                commands.logger.info ('> Applying jadretro')
                commands.applyjadretro(1)
                commands.logger.info ('> Decompiling...')
                commands.applyjad(1)
            commands.logger.info ('> Applying patches')
            if not use_ff:
                commands.applypatches(1)
            else:
                commands.applyffpatches(1)
            commands.logger.info ('> Renaming sources')
            commands.rename(1)
            commands.logger.info ('> Creating reobfuscation tables')
            commands.renamereobsrg(1)
            commands.logger.info ('> Done in %.2f seconds'%(time.time()-servertime))
    else:
        commands.logger.warn ('!! Server already decompiled. Run cleanup before decompiling again !!')
        srvdone = True

    commands.logger.info ('== Post decompiling operations ==')
    if not cltdone or not srvdone:
        commands.logger.info ('> Recompiling')
        recompile.main(conffile)
    if not cltdone:
        commands.logger.info ('> Generating the md5 (client)')
        commands.gathermd5s(0)
    if not srvdone:
        commands.logger.info ('> Generating the md5 (server)')
        commands.gathermd5s(1)

if __name__ == '__main__':
    parser = OptionParser(version='MCP %s' % Commands.MCPVersion)
    parser.add_option('-j', '--jad', dest='force_jad', action='store_true',
                      help='force use of JAD even if Fernflower available', default=False)
    parser.add_option('-c', '--config', dest='config', help='additional configuration file')
    (options, args) = parser.parse_args()
    main(options.config, options.force_jad)
