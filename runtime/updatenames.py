# -*- coding: utf-8 -*-
"""
Created on Fri Apr  8 16:54:36 2011

@author: Searge
@version: v1.0
"""
import sys, time, os
from optparse import OptionParser
from commands import Commands

def main(conffile):
    commands = Commands(conffile)

    commands.logger.info ('== Client ==')
    commands.logger.info ('> Renaming sources')
    commands.rename(0)
    commands.logger.info ('> Creating reobfuscation tables')
    commands.renamereobsrg(0)
    commands.logger.info ('== Server ==')
    commands.logger.info ('> Renaming sources')
    commands.rename(1)
    commands.logger.info ('> Creating reobfuscation tables')
    commands.renamereobsrg(1)

if __name__ == '__main__':
    parser = OptionParser(version='MCP %s' % Commands.MCPVersion)
    parser.add_option('-c', '--config', dest='config', help='additional configuration file')
    (options, args) = parser.parse_args()
    main(options.config)
